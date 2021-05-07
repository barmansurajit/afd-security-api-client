package com.schneider;

import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AfdServiceImpl implements AfdService {

    private final WebClient webClient;

    @Autowired
    public AfdServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Option<List<SniUser>> getMembers(String role, String carrierId, String phoneNo) {
        String url = "http://external-user-security-service.dev-aks.sndrio.com/v1/users/";

//        phoneNo = "+1919840225710".replace("+","%2B");
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(url);
        if (null != role && !role.trim().isEmpty())
            uriComponentsBuilder.queryParam("roleName", role);
        if (null != carrierId && !carrierId.trim().isEmpty())
            uriComponentsBuilder.queryParam("carrierId", carrierId);
        if (null != phoneNo && !phoneNo.trim().isEmpty())
            uriComponentsBuilder.queryParam("mobileNumber", phoneNo);

        String uriString = uriComponentsBuilder.build().encode().toString();

        List<SniUser> members = webClient.get()
                .uri(uriString)
                .exchange()
                .flatMap(clientResponse -> {
                    if (clientResponse.statusCode().isError())
                        return Mono.empty();
                    return clientResponse.bodyToFlux(SniUser.class)
                            .collectList();
                })
                .block();

        if (null == members)
            return Option.none();

        return Option.of(members);
    }
}
