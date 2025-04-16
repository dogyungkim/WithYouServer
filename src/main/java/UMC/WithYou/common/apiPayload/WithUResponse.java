package UMC.WithYou.common.apiPayload;

import UMC.WithYou.common.apiPayload.code.BaseCode;
import UMC.WithYou.common.apiPayload.code.status.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class WithUResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;


    // generate Response for Success
    public static <T> WithUResponse<T> onSuccess(T result){
        return new WithUResponse<>(true, SuccessStatus._OK.getCode() , SuccessStatus._OK.getMessage(), result);
    }

    public static WithUResponse<Void> onSuccess_NoContent() {
        return new WithUResponse<>(true, SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), null);
    }

    public static <T> WithUResponse<T> of(BaseCode code, T result){
        return new WithUResponse<>(true, code.getReasonHttpStatus().getCode() , code.getReasonHttpStatus().getMessage(), result);
    }


    // generate Response for Failure
    public static <T> WithUResponse<T> onFailure(String code, String message, T data){
        return new WithUResponse<>(false, code, message, data);
    }
}
