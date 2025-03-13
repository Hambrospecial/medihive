package com.hamidspecial.medihive.auth.service;

import com.hamidspecial.medihive.auth.model.ApplicationParameter;
import com.hamidspecial.medihive.auth.repository.ApplicationParameterRepository;
import com.hamidspecial.medihive.exception.BadRequestException;
import com.hamidspecial.medihive.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationParameterService {

    private final ApplicationParameterRepository applicationParameterRepository;

    public Result<ApplicationParameter> getApplicationParameterByKey(String key) {
        return applicationParameterRepository.findByParamKey(key)
                .map(Result::success)
                .orElseThrow(() -> new BadRequestException("400", "Application parameter not found for key: " + key));
    }

    public Result<ApplicationParameter> getAllApplicationParameters(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<ApplicationParameter> result = applicationParameterRepository.findAll(pageable);
        return Result.success(result.getContent(), result.getNumber(), result.getSize(), result.getTotalElements());
    }

    public Result<ApplicationParameter> getApplicationParameterById(long id) {
        return applicationParameterRepository.findById(id)
                .map(Result::success)
                .orElseThrow(() -> new BadRequestException("400", "Application parameter not found for ID: " + id));
    }

    public void updateApplicationParameter(ApplicationParameter applicationParameter) {
        if (!applicationParameterRepository.existsById(applicationParameter.getId())) {
            throw new BadRequestException("400", "Application parameter not found for ID: " + applicationParameter.getId());
        }
        applicationParameterRepository.save(applicationParameter);
    }
}

