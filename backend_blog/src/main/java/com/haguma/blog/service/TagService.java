package com.haguma.blog.service;

import com.haguma.blog.dto.TagDto;
import com.haguma.blog.entity.Tag;
import com.haguma.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private TagDto convertToDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setSlug(tag.getSlug());
        return dto;
    }
}