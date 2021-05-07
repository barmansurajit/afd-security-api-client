package com.schneider;

import io.vavr.control.Option;

import java.util.List;

public interface AfdService {
    Option<List<SniUser>> getMembers(String role, String carrierId, String phone);
}
