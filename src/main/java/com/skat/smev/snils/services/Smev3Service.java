package com.skat.smev.snils.services;


import com.skat.smev.snils.domain.*;
import com.skat.smev.snils.model.SnilsByAdditionalDataRequest;
import com.skat.smev.snils.model.SnilsByAdditionalDataResponse;
import com.skat.smev.snils.transmitter.impl.ResponseTransmitterService;
import com.skat.smev.snils.transmitter.impl.Smev3AdapterService;
import com.skat.smev.snils.util.JsonUtil;
import com.skat.smev.snils.util.XmlUtil;
import com.skat.smev.snils.util.snils.SnilsRequestTransformer;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import java.nio.charset.Charset;
import java.text.ParseException;

/**
 * Сервис для осуществления взаимодействия данного сервиса-трансформатора
 * со СМЭВ-адаптером и с ВИС
 */
@Service
public class Smev3Service {

    private static final Logger LOGGER = Logger.getLogger(Smev3Service.class.getName());

    @Autowired
    private Smev3AdapterService smev3AdapterService;

    @Autowired
    private ResponseTransmitterService responseTransmitterService;

    /**
     * Метод преобразования и отправки запроса от ВИС и отправки в СМЭВ-адаптер
     * @param requestModel модель запроса в формате JSON
     * @return  возвращает сведения об успешности отправки запроса
     * @throws Exception
     */
    public String sendRequest(RequestModel requestModel) throws ParseException, JAXBException, DatatypeConfigurationException {
        final String adapterRequest = createXmlFromModel(requestModel);
        final String base64request = convertToBase64(adapterRequest);
        try {
            return smev3AdapterService.sendRequest(base64request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Error while sending request";
    }

    /**
     * Метод для приема ответа от СМЭВ-адаптера, его парсинга и отправки в ВИС
     * @param adapterResponse модель ответа от СМЭВ-адаптера
     * @return сведения об успешной отправке либо об ошибке отправки
     * @throws Exception
     */
    public String sendResponse(AdapterResponseModel adapterResponse)  {
        BaseMessageModel baseMessageModel = null;
        String stringMessage = "";
        try {
            baseMessageModel = parseResponseFromAdapter(adapterResponse);
            stringMessage = JsonUtil.stringify(baseMessageModel);
        } catch (Exception e) {
            LOGGER.error("Error while parsing adapter response: " + e.getMessage());
        }
        return responseTransmitterService.sendResponse(stringMessage);
    }

    /**
     * Метод выполняет преобразование строки данный в формат base64
     * @param request строка данных
     * @return строка данный в формате base64
     */
    private String convertToBase64(String request){
        byte[] bytesInfo = request.getBytes(Charset.forName("UTF-8"));
        return bytesInfo != null ? DatatypeConverter.printBase64Binary(bytesInfo) : "";
    }

    /**
     * Метод выполняет преобразование строки данный из формата base64
     * в cтроку UTF-8
     * @param xmlBase64 строка данных
     * @return строка данный UTF-8
     */
    private String getXmlFromBase64(String xmlBase64) {
        String request = "";
        if (xmlBase64.isEmpty()) {
            return request;
        }
        final String requestInBase64 = new String(DatatypeConverter.parseBase64Binary(xmlBase64), Charset.forName("UTF-8"));
        LOGGER.info("Decoded response from base64: " + requestInBase64);
        return requestInBase64;
    }

    /**
     * Метод выпоняет преобразование модели запроса от ВИС в формате SON
     * в модель вида сведений
     * @param model модель запроса от ВИС
     * @return  преобразованная модель вида сведений
     * @throws JAXBException
     * @throws ParseException
     * @throws DatatypeConfigurationException
     */
    private String createXmlFromModel(RequestModel model) throws JAXBException,
            ParseException, DatatypeConfigurationException {
        SnilsRequestTransformer snilsRequestTransformer = new SnilsRequestTransformer();
        SnilsByAdditionalDataRequest element = snilsRequestTransformer.createSnilsAdditionalByDataRequest(model);
        String xml = XmlUtil.jaxbObjectToXML(element);
        return xml;
    }

    /**
     * Метод выполняет преобразование ответа от СМЭВ-адаптера
     * из формата {@link SnilsByAdditionalDataRequest} в формат {@link BaseMessageModel}
     * @param adapterResponseModel ответ от СМЭВ-адаптера
     * @return формированный ответ для дальнейшей отправки в ВИС
     * @throws Exception
     */
    private BaseMessageModel parseResponseFromAdapter(AdapterResponseModel adapterResponseModel) throws Exception {
        LOGGER.info("Try to parse response from adapter");
        LOGGER.info("Response: " + adapterResponseModel);

        if(adapterResponseModel.getResponse() != null){
            String xml = getXmlFromBase64(adapterResponseModel.getResponse());
            SnilsRequestTransformer snilsRequestTransformer = new SnilsRequestTransformer();
            SnilsByAdditionalDataResponse responseType = snilsRequestTransformer.parseContent(xml);
            String responseNumber = String.valueOf(responseType.getSnils());
            ResponseMessageModel responseMessageModel = new ResponseMessageModel();
            responseMessageModel.setResponseNumber(responseNumber);
            responseMessageModel.setMessageId(adapterResponseModel.getMessageId());
            return responseMessageModel;
        } else if(adapterResponseModel.getRejects() != null && !adapterResponseModel.getRejects().isEmpty()){
            RejectMessageModel rejectMessageModel = new RejectMessageModel();
            rejectMessageModel.setMessageId(adapterResponseModel.getMessageId());
            rejectMessageModel.setRejects(adapterResponseModel.getRejects());
            return rejectMessageModel;
        } else {
            StatusMessageModel statusMessageModel = new StatusMessageModel();
            statusMessageModel.setMessageId(adapterResponseModel.getMessageId());
            statusMessageModel.setDescription(adapterResponseModel.getDescription());
            return statusMessageModel;
        }
    }
}
