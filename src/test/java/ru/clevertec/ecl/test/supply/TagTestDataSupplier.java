package ru.clevertec.ecl.test.supply;

import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.model.Tag;

public class TagTestDataSupplier {

    public static final long DEFAULT_ID = 1L;
    public static final String DEFAULT_NAME = "default-tag";

    public static Tag getTag() {
        return new Tag(DEFAULT_ID, DEFAULT_NAME);
    }

    public static TagPutDto getTagPutDto() {
        return new TagPutDto(DEFAULT_NAME);
    }

    public static Tag getTagMappedFromPutDto() {
        return new Tag(0, DEFAULT_NAME);
    }
}
