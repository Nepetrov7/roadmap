package com.example.fms.config;

import com.example.fms.entity.RoadmapItem;
import com.example.fms.repository.RoadmapItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
  private final RoadmapItemRepository roadmapItemRepository;

  @Override
  public void run(String... args) {
    // Инициализация пунктов дорожной карты для сертификата владения русским языком
    if (roadmapItemRepository.count() == 0) {
      // Пункты для сертификата владения русским языком
      RoadmapItem item1 = new RoadmapItem();
      item1.setName("Подать заявку в отделение ФМС");
      item1.setChapter("Сертификат владения русским языком");
      item1.setOrderInChapter(1);
      roadmapItemRepository.save(item1);

      RoadmapItem item2 = new RoadmapItem();
      item2.setName("Прийти на экзамен в назначенную дату");
      item2.setChapter("Сертификат владения русским языком");
      item2.setOrderInChapter(2);
      roadmapItemRepository.save(item2);

      RoadmapItem item3 = new RoadmapItem();
      item3.setName("Получить сертификат после успешной сдачи экзамена");
      item3.setChapter("Сертификат владения русским языком");
      item3.setOrderInChapter(3);
      roadmapItemRepository.save(item3);

      // Пункты для патента на работу
      RoadmapItem item4 = new RoadmapItem();
      item4.setName("Собрать необходимые документы");
      item4.setChapter("Патент на работу");
      item4.setOrderInChapter(1);
      roadmapItemRepository.save(item4);

      RoadmapItem item5 = new RoadmapItem();
      item5.setName("Подать заявление на получение патента");
      item5.setChapter("Патент на работу");
      item5.setOrderInChapter(2);
      roadmapItemRepository.save(item5);

      RoadmapItem item6 = new RoadmapItem();
      item6.setName("Дождаться рассмотрения заявления");
      item6.setChapter("Патент на работу");
      item6.setOrderInChapter(3);
      roadmapItemRepository.save(item6);

      RoadmapItem item7 = new RoadmapItem();
      item7.setName("Получить патент на работу");
      item7.setChapter("Патент на работу");
      item7.setOrderInChapter(4);
      roadmapItemRepository.save(item7);

      // Пункты для оплаты госпошлины
      RoadmapItem item8 = new RoadmapItem();
      item8.setName("Получить реквизиты для оплаты госпошлины");
      item8.setChapter("Оплата госпошлины за патент");
      item8.setOrderInChapter(1);
      roadmapItemRepository.save(item8);

      RoadmapItem item9 = new RoadmapItem();
      item9.setName("Оплатить госпошлину в банке или через онлайн-сервис");
      item9.setChapter("Оплата госпошлины за патент");
      item9.setOrderInChapter(2);
      roadmapItemRepository.save(item9);

      RoadmapItem item10 = new RoadmapItem();
      item10.setName("Предоставить квитанцию об оплате в ФМС");
      item10.setChapter("Оплата госпошлины за патент");
      item10.setOrderInChapter(3);
      roadmapItemRepository.save(item10);
    }
  }
}
