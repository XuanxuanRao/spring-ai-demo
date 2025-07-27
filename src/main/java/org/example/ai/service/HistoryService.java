package org.example.ai.service;

import org.example.ai.domain.History;
import org.example.ai.enums.BusinessType;

import java.util.List;

/**
 * @author chenxuanrao06@gmail.com
 */
public interface HistoryService {

    List<History> getHistory(BusinessType type);

    History getHistoryById(BusinessType type, String id);

    void saveHistory(BusinessType type, String id);

}
