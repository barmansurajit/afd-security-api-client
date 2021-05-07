package com.schneider;

import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AfdServiceImplTest {

    @Mock
    private ExchangeFunction  exchangeFunction;

    @Captor
    private ArgumentCaptor<ClientRequest> captor;

    private WebClient.Builder builder;
    private AfdService afdService;

    @Test
    void getMembers_data_returned() throws Exception {

        // arrange
        ClientResponse clientResponse = ClientResponse.create(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body("[\n" +
                        "  {\n" +
                        "    \"firstName\": \"Joanne\",\n" +
                        "    \"lastName\": \"Smith\",\n" +
                        "    \"companyName\": \"TruckerStop\",\n" +
                        "    \"liablePartyId\": \"123456\",\n" +
                        "    \"liablePartyName\": \"Bogus\",\n" +
                        "    \"lastSelectedCustomer\": \"Henry\",\n" +
                        "    \"lastSelectedCarrier\": \"CRR\"\n" +
                        "  }" +
                        "]")
                .build();

        given(this.exchangeFunction.exchange(this.captor.capture())).willReturn(Mono.just(clientResponse));
        this.builder = WebClient.builder().exchangeFunction(this.exchangeFunction);
        afdService = new AfdServiceImpl(this.builder.build());

        // act
        Option<List<SniUser>> members = afdService.getMembers("role", "carrierId", "phone");

        // assert
        assertFalse(members.isEmpty());
    }

    @Test
    void getMembers_no_data_returned() throws Exception {

        // arrange
        ClientResponse clientResponse = ClientResponse.create(HttpStatus.NOT_FOUND)
                .build();

        given(this.exchangeFunction.exchange(this.captor.capture())).willReturn(Mono.just(clientResponse));
        this.builder = WebClient.builder().exchangeFunction(this.exchangeFunction);
        afdService = new AfdServiceImpl(this.builder.build());

        // act
        Option<List<SniUser>> members = afdService.getMembers("role", "carrierId", "phone");

        // assert
        assertTrue(members.isEmpty());
    }
}