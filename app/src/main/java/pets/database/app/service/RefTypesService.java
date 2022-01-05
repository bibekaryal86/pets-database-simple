package pets.database.app.service;

import lombok.extern.slf4j.Slf4j;
import pets.database.app.model.*;
import pets.database.app.repository.RefTypesDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static pets.database.app.util.Util.hasText;

@Slf4j
public class RefTypesService {

    private RefAccountType convertDtoToObject(RefAccountTypeDto refAccountTypeDto) {
        return RefAccountType.builder()
                .id(refAccountTypeDto.getId().toString())
                .description(refAccountTypeDto.getDescription())
                .creationDate(refAccountTypeDto.getCreationDate())
                .lastModified(refAccountTypeDto.getLastModified())
                .build();
    }

    private RefBank convertDtoToObject(RefBankDto refBankDto) {
        return RefBank.builder()
                .id(refBankDto.getId().toString())
                .description(refBankDto.getDescription())
                .creationDate(refBankDto.getCreationDate())
                .lastModified(refBankDto.getLastModified())
                .build();
    }

    private RefCategoryType convertDtoToObject(RefCategoryTypeDto refCategoryTypeDto) {
        return RefCategoryType.builder()
                .id(refCategoryTypeDto.getId().toString())
                .description(refCategoryTypeDto.getDescription())
                .creationDate(refCategoryTypeDto.getCreationDate())
                .lastModified(refCategoryTypeDto.getLastModified())
                .build();
    }

    private RefCategory convertDtoToObject(RefCategoryDto refCategoryDto) {
        return RefCategory.builder()
                .id(refCategoryDto.getId().toString())
                .description(refCategoryDto.getDescription())
                .refCategoryType(convertDtoToObject(refCategoryDto.getRefCategoryType()))
                .creationDate(refCategoryDto.getCreationDate())
                .lastModified(refCategoryDto.getLastModified())
                .build();
    }

    private RefTransactionType convertDtoToObject(RefTransactionTypeDto refTransactionTypeDto) {
        return RefTransactionType.builder()
                .id(refTransactionTypeDto.getId().toString())
                .description(refTransactionTypeDto.getDescription())
                .creationDate(refTransactionTypeDto.getCreationDate())
                .lastModified(refTransactionTypeDto.getLastModified())
                .build();
    }

    public RefAccountTypeResponse getAllRefAccountTypes() {
        log.info("Before Get All Ref Account Types");
        List<RefAccountType> refAccountTypes = new ArrayList<>();
        Status status = null;

        try {
            List<RefAccountTypeDto> accountTypeDtoList = new RefTypesDao().getAllRefAccountTypes();
            refAccountTypes = accountTypeDtoList.stream()
                    .map(this::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(ref -> hasText(ref.getId()))
                    .collect(toList());
        } catch (Exception ex) {
            log.error("Get All Ref Account Types", ex);
            status = Status.builder()
                    .errMsg("Error Retrieving All Ref Account Types, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get All Ref Account Types: {}", refAccountTypes.size());
        return RefAccountTypeResponse.builder()
                .refAccountTypes(refAccountTypes)
                .status(status)
                .build();
    }

    public RefBankResponse getAllRefBanks() {
        log.info("Before Get All Ref Banks");
        List<RefBank> refBanks = new ArrayList<>();
        Status status = null;

        try {
            List<RefBankDto> refBankDtoList = new RefTypesDao().getAllRefBanks();
            refBanks = refBankDtoList.stream()
                    .map(this::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(ref -> hasText(ref.getId()))
                    .collect(toList());
        } catch (Exception ex) {
            log.error("Get All Ref Banks", ex);
            status = Status.builder()
                    .errMsg("Error Retrieving All Banks, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get All Ref Banks: {}", refBanks.size());
        return RefBankResponse.builder()
                .refBanks(refBanks)
                .status(status)
                .build();
    }

    public RefCategoryTypeResponse getAllRefCategoryTypes() {
        log.info("Before Get All Ref Category Types");
        List<RefCategoryType> refCategoryTypes = new ArrayList<>();
        Status status = null;

        try {
            List<RefCategoryTypeDto> refCategoryTypeDtoList = new RefTypesDao().getAllRefCategoryTypes();
            refCategoryTypes = refCategoryTypeDtoList.stream()
                    .map(this::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(ref -> hasText(ref.getId()))
                    .collect(toList());
        } catch (Exception ex) {
            log.error("Get All Ref Category Types", ex);
            status = Status.builder()
                    .errMsg("Error Retrieving All Category Types, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get All Ref Category Types: {}", refCategoryTypes.size());
        return RefCategoryTypeResponse.builder()
                .refCategoryTypes(refCategoryTypes)
                .status(status)
                .build();
    }

    public RefCategoryResponse getAllRefCategories() {
        log.info("Before Get All Ref Categories");
        List<RefCategory> refCategories = new ArrayList<>();
        Status status = null;

        try {
            List<RefCategoryDto> refCategoryDtoList = new RefTypesDao().getAllRefCategories();
            refCategories = refCategoryDtoList.stream()
                    .map(this::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(ref -> hasText(ref.getId()))
                    .collect(toList());
        } catch (Exception ex) {
            log.error("Get All Ref Categories", ex);
            status = Status.builder()
                    .errMsg("Error Retrieving All Categories, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get All Ref Categories: {}", refCategories.size());
        return RefCategoryResponse.builder()
                .refCategories(refCategories)
                .status(status)
                .build();
    }

    public RefTransactionTypeResponse getAllRefTransactionTypes() {
        log.info("Before Get All Ref Transaction Types");
        List<RefTransactionType> refTransactionTypes = new ArrayList<>();
        Status status = null;

        try {
            List<RefTransactionTypeDto> refTransactionTypeDtoList = new RefTypesDao().getAllRefTransactionTypes();
            refTransactionTypes = refTransactionTypeDtoList.stream()
                    .map(this::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(ref -> hasText(ref.getId()))
                    .collect(toList());
        } catch (Exception ex) {
            log.error("Get All Ref Transaction Types", ex);
            status = Status.builder()
                    .errMsg("Error Retrieving All Transaction Types, Please Try Again!!!")
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get All Ref Transaction Types: {}", refTransactionTypes.size());
        return RefTransactionTypeResponse.builder()
                .refTransactionTypes(refTransactionTypes)
                .status(status)
                .build();
    }
}
