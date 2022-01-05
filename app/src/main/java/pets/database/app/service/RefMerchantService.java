package pets.database.app.service;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pets.database.app.model.RefMerchant;
import pets.database.app.model.RefMerchantDto;
import pets.database.app.model.RefMerchantRequest;
import pets.database.app.model.RefMerchantResponse;
import pets.database.app.model.Status;
import pets.database.app.model.User;
import pets.database.app.model.UserDto;
import pets.database.app.repository.RefMerchantDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static pets.database.app.util.Util.hasText;

@Slf4j
public class RefMerchantService {

    private static final String ERROR_RETRIEVING_MERCHANT = "Error Retrieving Merchants, Please Try Again!!!";
    private static final String ERROR_SAVING_MERCHANT = "Error Saving Merchant, Please Try Again!!!";
    private static final String ERROR_UPDATING_MERCHANT = "Error Updating Merchant, Please Try Again!!!";
    private static final String ERROR_DELETING_MERCHANT = "Error Deleting Merchant, Please Try Again!!!";

    private RefMerchant convertDtoToObject(RefMerchantDto refMerchantDto) {
        return RefMerchant.builder()
                .id(refMerchantDto.getId() == null ? null : refMerchantDto.getId().toString())
                .description(refMerchantDto.getDescription())
                .user(User.builder()
                        .username(refMerchantDto.getUser().getUsername())
                        .build())
                .creationDate(refMerchantDto.getCreationDate())
                .lastModified(refMerchantDto.getLastModified())
                .build();
    }

    private RefMerchantDto convertObjectToDto(RefMerchantRequest refMerchantRequest) {
        return RefMerchantDto.builder()
                .description(refMerchantRequest.getDescription())
                .user(UserDto.builder()
                        .username(refMerchantRequest.getUsername())
                        .build())
                .build();
    }

    public RefMerchantResponse getAllRefMerchantsByUsername(String username) {
        log.info("Before Get All Ref Merchants By Username: {}", username);
        List<RefMerchant> refMerchants = new ArrayList<>();
        Status status = null;

        try {
            List<RefMerchantDto> refMerchantDtoList = new RefMerchantDao().getAllRefMerchantsByUsername(username);
            refMerchants = refMerchantDtoList.stream()
                    .map(this::convertDtoToObject)
                    .filter(Objects::nonNull)
                    .filter(refMerchant -> hasText(refMerchant.getId()))
                    .collect(toList());
        } catch (Exception ex) {
            log.error("Get All Ref Merchants By Username: {}", username, ex);
            status = Status.builder()
                    .errMsg(ERROR_RETRIEVING_MERCHANT)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get All Ref Merchants By Username: {}", refMerchants.size());
        return RefMerchantResponse.builder()
                .refMerchants(refMerchants)
                .status(status)
                .build();
    }

    public RefMerchantResponse getRefMerchantById(String id) {
        log.info("Before Get Ref Merchant By Id: {}", id);
        RefMerchant refMerchant = null;
        Status status = null;

        try {
            RefMerchantDto refMerchantDto = new RefMerchantDao().getRefMerchantById(id);
            if (refMerchantDto != null) {
                refMerchant = convertDtoToObject(refMerchantDto);
            }
        } catch (Exception ex) {
            log.error("Get Ref Merchant By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_RETRIEVING_MERCHANT)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Get Ref Merchant By Id: {}", refMerchant);
        return RefMerchantResponse.builder()
                .refMerchants(refMerchant == null ? emptyList() : List.of(refMerchant))
                .status(status)
                .build();
    }

    public RefMerchantResponse saveNewRefMerchant(RefMerchantRequest refMerchantRequest) {
        log.info("Before Save New Ref Merchant: {}", refMerchantRequest);
        RefMerchant refMerchant = null;
        Status status = null;

        try {
            RefMerchantDto refMerchantDto = convertObjectToDto(refMerchantRequest);
            refMerchantDto.setCreationDate(LocalDate.now().toString());
            refMerchantDto.setLastModified(LocalDateTime.now().toString());

            String insertedId = new RefMerchantDao().saveNewRefMerchant(refMerchantDto);
            if (hasText(insertedId)) {
                refMerchant = convertDtoToObject(refMerchantDto);
                refMerchant.setId(insertedId);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_SAVING_MERCHANT)
                        .build();
            }
        } catch (Exception ex) {
            log.error("Save New Ref Merchant: {}", refMerchantRequest, ex);
            status = Status.builder()
                    .errMsg(ERROR_SAVING_MERCHANT)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Save New Ref Merchant: {}", refMerchant);
        return RefMerchantResponse.builder()
                .refMerchants(refMerchant == null ? emptyList() : List.of(refMerchant))
                .status(status)
                .build();
    }

    public RefMerchantResponse updateRefMerchantById(String id, RefMerchantRequest refMerchantRequest) {
        log.info("Before Update Ref Merchant By Id: {} | {}", id, refMerchantRequest);
        RefMerchantResponse refMerchantResponse;
        Status status;

        try {
            Bson filter = Filters.eq("_id", new ObjectId(id));
            Bson updates = Updates.combine(List.of(
                    Updates.set("description", refMerchantRequest.getDescription()),
                    Updates.set("lastModified", LocalDateTime.now().toString())));

            long modifiedCount = new RefMerchantDao().updateRefMerchantById(filter, updates);

            if (modifiedCount > 0) {
                refMerchantResponse = getRefMerchantById(id);
            } else {
                status = Status.builder()
                        .errMsg(ERROR_UPDATING_MERCHANT)
                        .build();
                refMerchantResponse = RefMerchantResponse.builder()
                        .refMerchants(emptyList())
                        .status(status)
                        .build();
            }
        } catch (Exception ex) {
            log.error("Update Ref Merchant By Id: {} | {}", id, refMerchantRequest, ex);
            status = Status.builder()
                    .errMsg(ERROR_UPDATING_MERCHANT)
                    .message(ex.toString())
                    .build();
            refMerchantResponse = RefMerchantResponse.builder()
                    .refMerchants(emptyList())
                    .status(status)
                    .build();
        }

        log.info("After Update Ref Merchant By Id: {} | user: {}", id, refMerchantResponse);
        return refMerchantResponse;
    }

    public RefMerchantResponse deleteRefMerchantById(String id) {
        log.info("Before Delete Ref Merchant By Id: {}", id);
        long deleteCount = 0;
        Status status = null;

        try {
            Bson filter = Filters.eq("_id", new ObjectId(id));
            deleteCount = new RefMerchantDao().deleteRefMerchantById(filter);
        } catch (Exception ex) {
            log.error("Delete Ref Merchant By Id: {}", id, ex);
            status = Status.builder()
                    .errMsg(ERROR_DELETING_MERCHANT)
                    .message(ex.toString())
                    .build();
        }

        log.info("After Delete Ref Merchant By Id: {} | deleteCount: {}", id, deleteCount);
        return RefMerchantResponse.builder()
                .refMerchants(emptyList())
                .deleteCount(deleteCount)
                .status(status)
                .build();
    }
}
