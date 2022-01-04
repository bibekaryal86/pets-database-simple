package pets.database.app.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pets.database.app.model.*;
import pets.database.app.repository.RefTypesDao;
import pets.database.app.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefTypesService {

    private static RefAccountType convertDtoToObject(RefAccountTypeDto refAccountTypeDto) {
        return RefAccountType.builder()
                .id(refAccountTypeDto.getId().toString())
                .description(refAccountTypeDto.getDescription())
                .build();
    }

    private static RefBank convertDtoToObject(RefBankDto refBankDto) {
        return RefBank.builder()
                .id(refBankDto.getId().toString())
                .description(refBankDto.getDescription())
                .build();
    }

    private static RefCategoryType convertDtoToObject(RefCategoryTypeDto refCategoryTypeDto) {
        return RefCategoryType.builder()
                .id(refCategoryTypeDto.getId().toString())
                .description(refCategoryTypeDto.getDescription())
                .build();
    }

    private static RefCategory convertDtoToObject(RefCategoryDto refCategoryDto) {
        return RefCategory.builder()
                .id(refCategoryDto.getId().toString())
                .description(refCategoryDto.getDescription())
                .refCategoryType(convertDtoToObject(refCategoryDto.getRefCategoryType()))
                .build();
    }

    private static RefTransactionType convertDtoToObject(RefTransactionTypeDto refTransactionTypeDto) {
        return RefTransactionType.builder()
                .id(refTransactionTypeDto.getId().toString())
                .description(refTransactionTypeDto.getDescription())
                .build();
    }

    public static RefAccountTypeResponse getAllRefAccountTypes() {
        log.info("Before Get All Ref Account Types");
        List<RefAccountType> refAccountTypes = new ArrayList<>();
        Status status = null;

        try {
            List<RefAccountTypeDto> accountTypeDtoList = RefTypesDao.getAllRefAccountTypes();
            refAccountTypes = accountTypeDtoList.stream()
                    .map(RefTypesService::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(ref -> Util.hasText(ref.getId()))
                    .collect(Collectors.toList());
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

    public static RefBankResponse getAllRefBanks() {
        log.info("Before Get All Ref Banks");
        List<RefBank> refBanks = new ArrayList<>();
        Status status = null;

        try {
            List<RefBankDto> refBankDtoList = RefTypesDao.getAllRefBanks();
            refBanks = refBankDtoList.stream()
                    .map(RefTypesService::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(ref -> Util.hasText(ref.getId()))
                    .collect(Collectors.toList());
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

    public static RefCategoryTypeResponse getAllRefCategoryTypes() {
        log.info("Before Get All Ref Category Types");
        List<RefCategoryType> refCategoryTypes = new ArrayList<>();
        Status status = null;

        try {
            List<RefCategoryTypeDto> refCategoryTypeDtoList = RefTypesDao.getAllRefCategoryTypes();
            refCategoryTypes = refCategoryTypeDtoList.stream()
                    .map(RefTypesService::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(ref -> Util.hasText(ref.getId()))
                    .collect(Collectors.toList());
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

    public static RefCategoryResponse getAllRefCategories() {
        log.info("Before Get All Ref Categories");
        List<RefCategory> refCategories = new ArrayList<>();
        Status status = null;

        try {
            List<RefCategoryDto> refCategoryDtoList = RefTypesDao.getAllRefCategories();
            refCategories = refCategoryDtoList.stream()
                    .map(RefTypesService::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(ref -> Util.hasText(ref.getId()))
                    .collect(Collectors.toList());
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

    public static RefTransactionTypeResponse getAllRefTransactionTypes() {
        log.info("Before Get All Ref Transaction Types");
        List<RefTransactionType> refTransactionTypes = new ArrayList<>();
        Status status = null;

        try {
            List<RefTransactionTypeDto> refTransactionTypeDtoList = RefTypesDao.getAllRefTransactionTypes();
            refTransactionTypes = refTransactionTypeDtoList.stream()
                    .map(RefTypesService::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(ref -> Util.hasText(ref.getId()))
                    .collect(Collectors.toList());
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
