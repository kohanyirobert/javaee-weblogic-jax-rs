package com.codecool.bank;

import com.codecool.bank.dto.*;
import com.codecool.bank.service.BankcardService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.external.ExternalTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainer;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Test;

import javax.json.Json;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.net.URI;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BankcardResourceTest extends JerseyTest {

    private static final URI BASE_URI = URI.create("http://localhost:7001/bank");

    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new ExternalTestContainerFactory() {

            @Override
            public TestContainer create(URI baseUri, DeploymentContext context) throws IllegalArgumentException {
                return super.create(baseUri.resolve(BASE_URI), context);
            }
        };
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(BankcardService.class);
    }

    @Test
    public void validateOk() {
        // create
        String cardNumber = generateNewCardNumber();
        NewBankcardDto bankdcardDto = createSampleNewBankcardDto();
        bankdcardDto.setCardNumber(cardNumber);
        assertCreateCard(bankdcardDto, Response.Status.OK);

        // validate (VALID)
        ValidateBankcardDto validateBankcardDto = createValidateBankcardDtoFrom(bankdcardDto);

        Response response = target("/cards/validate")
            .request()
            .post(Entity.json(validateBankcardDto));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        BankcardValidationResultDto validationResultDto = response.readEntity(BankcardValidationResultDto.class);

        assertEquals("VALID", validationResultDto.getResult());
    }

    @Test
    public void validateOkInvalid() {
        // create
        String cardNumber = generateNewCardNumber();
        NewBankcardDto bankdcardDto = createSampleNewBankcardDto();
        bankdcardDto.setCardNumber(cardNumber);
        assertCreateCard(bankdcardDto, Response.Status.OK);

        // validate (INVALID CVV)
        ValidateBankcardDto validateBankcardDto = createValidateBankcardDtoFrom(bankdcardDto);
        validateBankcardDto.setCvv("666");
        assertValidation(validateBankcardDto, false);

        // validate (INVALID validThru)
        validateBankcardDto = createValidateBankcardDtoFrom(bankdcardDto);
        validateBankcardDto.setValidThru("12/12");
        assertValidation(validateBankcardDto, false);

        // validate (INVALID cardNumber)
        validateBankcardDto = createValidateBankcardDtoFrom(bankdcardDto);
        validateBankcardDto.setCardNumber(generateNewCardNumber());
        assertValidation(validateBankcardDto, false);

        // validate (INVALID cardType)
        validateBankcardDto = createValidateBankcardDtoFrom(bankdcardDto);
        validateBankcardDto.setCardType("Mastercard");
        assertValidation(validateBankcardDto, false);
    }

    @Test
    public void disableCardFailsWhenMissing() {
        String cardNumber = generateNewCardNumber();
        Response response = target("/cards/{cardNumber}")
            .resolveTemplate("cardNumber", cardNumber)
            .request()
            .put(Entity.json(""));
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void disableCardOk() {
        // create
        String cardNumber = generateNewCardNumber();
        NewBankcardDto dto = createSampleNewBankcardDto();
        dto.setCardNumber(cardNumber);
        assertCreateCard(dto, Response.Status.OK);

        // disable
        Response response = target("/cards/{cardNumber}")
            .resolveTemplate("cardNumber", cardNumber)
            .request()
            .put(Entity.json(""));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // getAll
        BankcardDto result = target("/cards/{cardNumber}")
            .resolveTemplate("cardNumber", cardNumber)
            .request(MediaType.APPLICATION_JSON)
            .get(BankcardDto.class);

        assertTrue(result.isDisabled());
    }

    @Test
    public void getAllCardsReturnsJsonArray() {
        String response = target("/cards").request().get(String.class);
        Json.createReader(new StringReader(response)).readArray();
    }

    @Test
    public void getCardOk() {
        // create
        String cardNumber = generateNewCardNumber();
        NewBankcardDto dto = createSampleNewBankcardDto();
        dto.setCardNumber(cardNumber);
        assertCreateCard(dto, Response.Status.OK);

        // getAll
        Response response = target("/cards/{cardNumber}")
            .resolveTemplate("cardNumber", cardNumber)
            .request(MediaType.APPLICATION_JSON)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        BankcardDto result = response.readEntity(BankcardDto.class);
        assertEquals(dto.getCardNumber(), result.getCardNumber());
        assertEquals(dto.getCardType(), result.getCardType());
        assertEquals(dto.getOwner(), result.getOwner());
        assertEquals(dto.getValidThru(), result.getValidThru());
        assertEquals(dto.getContactInfo(), result.getContactInfo());
    }

    @Test
    public void getCardFailsWhenMissing() {
        String cardNumber = generateNewCardNumber();
        Response response = target("/cards/{cardNumber}")
            .resolveTemplate("cardNumber", cardNumber)
            .request(MediaType.APPLICATION_JSON)
            .get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void createCardOk() {
        NewBankcardDto dto = createSampleNewBankcardDto();
        dto.setCardNumber(generateNewCardNumber());
        assertCreateCard(dto, Response.Status.OK);
    }

    @Test
    public void createCardFailsWhenExistingCardNumber() {
        String cardNumber = generateNewCardNumber();

        NewBankcardDto dto1 = createSampleNewBankcardDto();
        NewBankcardDto dto2 = createSampleNewBankcardDto();

        dto1.setCardNumber(cardNumber);
        dto2.setCardNumber(cardNumber);

        assertCreateCard(dto1, Response.Status.OK);
        assertCreateCard(dto2, Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void createCardFailsWhenEmpty() {
        assertCreateCard(new NewBankcardDto(), Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void createCardFailsWhenInvalidCardNumber() {
        NewBankcardDto dto = createSampleNewBankcardDto();
        dto.setCardNumber("invalid");
        assertCreateCard(dto, Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void createCardFailsWhenInvalidCvv() {
        NewBankcardDto dto = createSampleNewBankcardDto();
        dto.setCvv("invalid");
        assertCreateCard(dto, Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void createCardFailsWhenInvalidOwner() {
        NewBankcardDto dto = createSampleNewBankcardDto();
        dto.setOwner("");
        assertCreateCard(dto, Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void createCardFailsWhenInvalidCardType() {
        NewBankcardDto dto = createSampleNewBankcardDto();
        dto.setCardType("invalid");
        assertCreateCard(dto, Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void createCardFailsWhenInvalidValidThru() {
        NewBankcardDto dto = createSampleNewBankcardDto();
        dto.setValidThru("invalid");
        assertCreateCard(dto, Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void createCardFailsWhenInvalidContactType() {
        NewBankcardDto dto = createSampleNewBankcardDto();
        dto.getContactInfo().get(0).setType("invalid");
        assertCreateCard(dto, Response.Status.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void createCardFailsWhenInvalidContact() {
        NewBankcardDto dto = createSampleNewBankcardDto();
        dto.getContactInfo().get(0).setContact("");
        assertCreateCard(dto, Response.Status.INTERNAL_SERVER_ERROR);
    }

    private void assertCreateCard(NewBankcardDto dto, Response.Status expectedStatus) {
        Response response = target("/cards")
            .request()
            .post(Entity.json(dto));
        assertEquals(expectedStatus.getStatusCode(), response.getStatus());
    }

    private void assertValidation(ValidateBankcardDto dto, boolean expectedResult) {
        Response response = target("/cards/validate")
            .request()
            .post(Entity.json(dto));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        BankcardValidationResultDto validationResultDto = response.readEntity(BankcardValidationResultDto.class);

        assertEquals(expectedResult ? "VALID" : "INVALID", validationResultDto.getResult());
    }

    private NewBankcardDto createSampleNewBankcardDto() {
        NewBankcardDto newBankcardDto = new NewBankcardDto();
        newBankcardDto.setCardType("Visa");
        newBankcardDto.setCardNumber("1111-2222-3333-4444");
        newBankcardDto.setValidThru("01/24");
        newBankcardDto.setCvv("012");
        newBankcardDto.setOwner("Joe Smith");

        ContactInfoDto contactInfoDto1 = new ContactInfoDto();
        contactInfoDto1.setType("SMS");
        contactInfoDto1.setContact("+123456789");

        ContactInfoDto contactInfoDto2 = new ContactInfoDto();
        contactInfoDto2.setType("EMAIL");
        contactInfoDto2.setContact("joe.smith@sample.com");

        newBankcardDto.setContactInfo(Arrays.asList(contactInfoDto1, contactInfoDto2));
        return newBankcardDto;
    }

    private ValidateBankcardDto createValidateBankcardDtoFrom(NewBankcardDto newBankdcardDto) {
        ValidateBankcardDto validateBankcardDto = new ValidateBankcardDto();
        validateBankcardDto.setCardNumber(newBankdcardDto.getCardNumber());
        validateBankcardDto.setCardType(newBankdcardDto.getCardType());
        validateBankcardDto.setCvv(newBankdcardDto.getCvv());
        validateBankcardDto.setValidThru(newBankdcardDto.getValidThru());
        return validateBankcardDto;
    }

    private String generateNewCardNumber() {
        Random random = new Random();
        return IntStream.range(0, 4)
            .mapToObj(i -> String.format("%04d", random.nextInt(10000)))
            .collect(Collectors.joining("-"));
    }
}
