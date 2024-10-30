package com.cmiethling.mplex.client.controller;

import com.cmiethling.mplex.client_api.openapi.api.HvApi;
import com.cmiethling.mplex.client_api.openapi.model.hv.ReadCapillaryDataRequest;
import com.cmiethling.mplex.client_api.openapi.model.hv.ReadCapillaryDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HighVoltageController implements HvApi {

    @Override
    public ResponseEntity<ReadCapillaryDataResponse> sendReadCapillaryDataCommand(final ReadCapillaryDataRequest readCapillaryDataRequest) {
        return ResponseEntity.ok(HvApi.super.sendReadCapillaryDataCommand(readCapillaryDataRequest).getBody());
    }
}