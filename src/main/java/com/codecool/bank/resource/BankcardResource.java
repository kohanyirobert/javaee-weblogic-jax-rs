package com.codecool.bank.resource;

import com.codecool.bank.dto.*;
import com.codecool.bank.entity.Bankcard;
import com.codecool.bank.service.BankcardService;
import com.codecool.bank.service.ContactPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("cards")
public class BankcardResource {

    private static final Logger LOG = LoggerFactory.getLogger(BankcardResource.class);

    @Inject
    private BankcardService bankcardService;

    @Inject
    private ContactPersonService contactPersonService;

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(NewBankcardDto dto) {
        LOG.debug("create({})", dto);
        Bankcard bankcard = bankcardService.create(
            dto.getCardType(),
            dto.getCardNumber(),
            dto.getCvv(),
            dto.getOwner(),
            dto.getValidThru());

        for (ContactInfoDto ciDto : dto.getContactInfo()) {
            contactPersonService.create(bankcard, ciDto.getType(), ciDto.getContact());
        }

        return Response.ok().build();
    }

    @Path("validate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BankcardValidationResultDto validate(ValidateBankcardDto dto) {
        LOG.debug("validate({})", dto);
        boolean result = bankcardService.validate(dto.getCardType(), dto.getCardNumber(), dto.getCvv(), dto.getValidThru());
        return new BankcardValidationResultDto(result);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BankcardDto> getAll() {
        LOG.debug("getAll()");
        return bankcardService.getAll()
            .stream()
            .map(BankcardDto::new)
            .collect(Collectors.toList());
    }

    @GET
    @Path("{cardNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("cardNumber") String cardNumber) {
        LOG.debug("get({})", cardNumber);
        try {
            return Response.ok(new BankcardDto(bankcardService.get(cardNumber))).build();
        } catch (NoResultException ex) {
            LOG.warn(String.format("get(%s)", cardNumber), ex);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Transactional
    @PUT
    @Path("{cardNumber}")
    public Response disable(@PathParam("cardNumber") String cardNumber) {
        LOG.debug("disable({})", cardNumber);
        try {
            bankcardService.disable(cardNumber);
            return Response.ok().build();
        } catch (NoResultException ex) {
            LOG.warn(String.format("disable(%s)", cardNumber), ex);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
