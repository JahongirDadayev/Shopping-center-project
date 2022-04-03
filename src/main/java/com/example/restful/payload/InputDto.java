package com.example.restful.payload;

import com.example.restful.status.InputStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputDto {

    private Integer warehouseId;

    private Integer supplierId;

    private Integer currencyId;

    private String factureNumber;

    private List<InputProductDto> inputProductDtoList;

    private InputStatus inputStatus;
}
