package com.mediconnect.office.api;
import java.util.UUID; public record OfficeResponse(UUID id,String name,String address,String roomNumber,boolean active) {}
