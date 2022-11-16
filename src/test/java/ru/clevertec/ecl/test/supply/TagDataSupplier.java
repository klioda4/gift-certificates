package ru.clevertec.ecl.test.supply;

import java.util.Collections;
import java.util.List;
import ru.clevertec.ecl.dto.request.TagCreateDto;
import ru.clevertec.ecl.dto.request.TagPutDto;
import ru.clevertec.ecl.dto.response.TagDto;
import ru.clevertec.ecl.model.Tag;

public class TagDataSupplier {

    public static final long DEFAULT_ID = 1L;
    public static final String DEFAULT_NAME = "default-tag";

    public static Tag getTag() {
        return new Tag(DEFAULT_ID, DEFAULT_NAME);
    }

    public static TagDto getTagDto() {
        return new TagDto(DEFAULT_ID, DEFAULT_NAME);
    }

    public static TagCreateDto getTagCreateDto() {
        return new TagCreateDto(DEFAULT_ID, DEFAULT_NAME);
    }

    public static TagPutDto getTagPutDto() {
        return new TagPutDto(DEFAULT_NAME);
    }

    public static List<Tag> getListOfSingleTag() {
        return Collections.singletonList(getTag());
    }
}
