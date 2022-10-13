package ru.clevertec.ecl.controller.swagger;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.Getter;

@Getter
public class PageableRequestDescription {

    @ApiParam(value = "Pagination page number",
              example = "0",
              defaultValue = "0")
    private int page;

    @ApiParam(value = "Pagination page size",
              example = "20",
              defaultValue = "20")
    private int size;

    @ApiParam(value = "Sort type",
              allowMultiple = true,
              examples = @Example({
                  @ExampleProperty("name,DESC"),
                  @ExampleProperty("id")}))
    private String sort;
}
