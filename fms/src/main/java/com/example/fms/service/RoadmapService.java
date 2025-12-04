package com.example.fms.service;

import com.example.fms.dto.RoadmapItemDto;
import com.example.fms.dto.RoadmapResponse;
import com.example.fms.entity.RoadmapItem;
import com.example.fms.entity.User;
import com.example.fms.repository.RoadmapItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoadmapService {
    private final UserService userService;
    private final RoadmapItemRepository roadmapItemRepository;

    public RoadmapResponse getRoadmap() {
        // Получаем пользователя из базы для проверки сертификатов и патентов
        User user = userService.getOrCreateUser();

        // Проверяем, заполнен ли профиль
        boolean isProfileComplete = user.getCountryOfArrival() != null && 
                                   user.getArrivalDate() != null;

        if (!isProfileComplete) {
            return new RoadmapResponse(
                    new HashMap<>(),
                    false,
                    "Для получения дорожной карты необходимо заполнить профиль"
            );
        }

        // Определяем, какие главы нужно включить в дорожную карту
        Set<String> requiredChapters = new LinkedHashSet<>();

        // Приоритет 1: Сертификат владения русским языком (самое важное)
        if (user.getRussianLanguageCertificate() == null) {
            requiredChapters.add("Сертификат владения русским языком");
        }

        // Приоритет 2: Патент на работу
        if (user.getWorkPatent() == null) {
            requiredChapters.add("Патент на работу");
            // Если патента нет, то после его получения нужно будет оплатить пошлину
            requiredChapters.add("Оплата госпошлины за патент");
        } else {
            // Приоритет 3: Оплата госпошлины за патент (если патент есть, но пошлина не оплачена)
            if (!Boolean.TRUE.equals(user.getWorkPatent().getStateDutyPaid())) {
                requiredChapters.add("Оплата госпошлины за патент");
            }
        }

        // Получаем все пункты дорожной карты для требуемых глав
        Map<String, List<RoadmapItemDto>> roadmapByChapter = new LinkedHashMap<>();
        
        for (String chapter : requiredChapters) {
            List<RoadmapItem> items = roadmapItemRepository.findByChapterOrderByOrderInChapterAsc(chapter);
            List<RoadmapItemDto> itemDtos = items.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            
            if (!itemDtos.isEmpty()) {
                roadmapByChapter.put(chapter, itemDtos);
            }
        }

        String message = roadmapByChapter.isEmpty() 
                ? "Все необходимые документы оформлены!" 
                : "Ваша дорожная карта действий";

        return new RoadmapResponse(roadmapByChapter, true, message);
    }

    private RoadmapItemDto mapToDto(RoadmapItem item) {
        RoadmapItemDto dto = new RoadmapItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setChapter(item.getChapter());
        dto.setOrderInChapter(item.getOrderInChapter());
        return dto;
    }
}

