package com.java.recruitment.web.controller.interviewer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.service.IJobRequestService;
import com.java.recruitment.service.filter.CriteriaModel;
import com.java.recruitment.web.dto.hiring.ChangeJobRequestStatusDTO;
import com.java.recruitment.web.dto.hiring.JobResponseDTO;
import com.java.recruitment.web.mapper.impl.JobRequestMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/interviewer/job-request")
@RequiredArgsConstructor
@Tag(
        name = "INTERVIEWER - JOB_REQUESTS",
        description = "CRUD OPERATIONS WITH JOB-REQUESTS"
)
public class InterviewerJobRequestController {

    private final IJobRequestService jobRequestService;

    private final JobRequestRepository jobRequestRepository;

    private final JobRequestMapper mapper;

//    @GetMapping
//    public ResponseEntity<Page<JobResponseDTO>> getAllJobRequests(
//            @RequestParam(required = false) List<String> criteria,
//            @ParameterObject Pageable pageable) {
//
//        List<CriteriaModel> foundCriteria = new ArrayList<>();
//
//        if (!criteria.isEmpty()) {
//            for (String current : criteria) {
//                String[] parts = current.split("&");
//                Map<String, String> params = new HashMap<>();
//                for (String part : parts) {
//                    String[] keyValue = part.split("=");
//                    params.put(keyValue[0], keyValue[1]);
//                }
//
//                CriteriaModel criteriaModel = new CriteriaModel();
//                criteriaModel.setField(params.get("field"));
//                criteriaModel.setOperation(Operation.valueOf(params.get("operation")));
//                criteriaModel.setValue(params.get("value"));
//                criteriaModel.setJoinType(JoinType.valueOf(params.get("joinType")));
//
//                foundCriteria.add(criteriaModel);
//            }
//        }
//
//        Page<JobResponseDTO> jobRequests;
//        if (!foundCriteria.isEmpty()) {
//            jobRequests = jobRequestService.getAllJobRequests(foundCriteria, pageable);
//        } else {
//            jobRequests = jobRequestRepository.findAll(pageable).map(mapper::toDto);
//        }
//
//        return new ResponseEntity<>(jobRequests, HttpStatus.OK);
//    }


    @GetMapping
    public ResponseEntity<Page<JobResponseDTO>> getAllJobRequests(
            @RequestParam(required = false) List<CriteriaModel> criteria,
            @ParameterObject Pageable pageable)
            throws BadRequestException {

        List<CriteriaModel> foundCriteria = new ArrayList<>();

        if (!criteria.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                for (CriteriaModel current : criteria) {
                    CriteriaModel criteriaModel = objectMapper.convertValue(current, CriteriaModel.class);
                    foundCriteria.add(criteriaModel);
                }
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Не удалось проанализировать условия", ex);
            }
        }

        Page<JobResponseDTO> jobRequests;
        if (!foundCriteria.isEmpty()) {
            jobRequests = jobRequestService.getAllJobRequests(foundCriteria, pageable);
        } else {
            jobRequests = jobRequestRepository.findAll(pageable).map(mapper::toDto);
        }

        return new ResponseEntity<>(jobRequests, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("@cse.canAccessJobRequest(#id)")
    public ResponseEntity<JobResponseDTO> getJobRequestById(@PathVariable Long id) {
        JobResponseDTO jobRequest = jobRequestService.getJobRequestById(id);
        return new ResponseEntity<>(jobRequest, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@cse.canAccessJobRequest(#jobRequestDto.id)")
    public ResponseEntity<JobResponseDTO> updateStatusJobRequest(@RequestBody ChangeJobRequestStatusDTO
                                                                         jobRequestDto) {
        JobResponseDTO updatedJobRequest = jobRequestService.updateJobRequest(jobRequestDto);
        return new ResponseEntity<>(updatedJobRequest, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobRequest(@PathVariable Long id) {
        jobRequestService.deleteJobRequest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}