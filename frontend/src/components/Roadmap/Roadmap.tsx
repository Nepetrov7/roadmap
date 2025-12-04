import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { roadmapService } from '../../services/roadmapService';
import { userService } from '../../services/userService';
import { RoadmapResponse, UserProfileResponse } from '../../types';
import styles from './Roadmap.module.scss';

export const Roadmap: React.FC = () => {
  const navigate = useNavigate();
  const [roadmap, setRoadmap] = useState<RoadmapResponse | null>(null);
  const [profile, setProfile] = useState<UserProfileResponse | null>(null);
  const [completedItems, setCompletedItems] = useState<Set<number>>(new Set());
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');
  const [updating, setUpdating] = useState<boolean>(false);

  useEffect(() => {
    const loadData = async (): Promise<void> => {
      try {
        const [roadmapData, profileData] = await Promise.all([
          roadmapService.getRoadmap(),
          userService.getProfile(),
        ]);
        setRoadmap(roadmapData);
        setProfile(profileData);

        // Загружаем сохраненные выполненные пункты из localStorage
        const savedCompleted = localStorage.getItem('completedItems');
        let completedSet: Set<number> = savedCompleted
          ? new Set<number>(JSON.parse(savedCompleted) as number[])
          : new Set<number>();

        // Автоматически отмечаем пункты как выполненные, если раздел уже выполнен в профиле
        if (roadmapData && profileData) {
          Object.entries(roadmapData.roadmapByChapter).forEach(
            ([chapter, items]) => {
              let shouldMarkCompleted = false;

              if (
                chapter === 'Сертификат владения русским языком' &&
                profileData.hasRussianLanguageCertificate
              ) {
                shouldMarkCompleted = true;
              } else if (
                chapter === 'Патент на работу' &&
                profileData.hasWorkPatent
              ) {
                shouldMarkCompleted = true;
              } else if (
                chapter === 'Оплата госпошлины за патент' &&
                profileData.hasPaidStateDuty
              ) {
                shouldMarkCompleted = true;
              }

              if (shouldMarkCompleted) {
                items.forEach((item) => completedSet.add(item.id));
              }
            }
          );
        }

        setCompletedItems(completedSet);

        // Сохраняем обновленный список
        localStorage.setItem(
          'completedItems',
          JSON.stringify(Array.from(completedSet))
        );
      } catch (err: any) {
        setError('Ошибка при загрузке данных');
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [navigate]);

  const handleGoToProfile = (): void => {
    navigate('/');
  };

  const handleItemToggle = async (
    itemId: number,
    chapter: string
  ): Promise<void> => {
    const newCompleted = new Set(completedItems);

    if (newCompleted.has(itemId)) {
      newCompleted.delete(itemId);
    } else {
      newCompleted.add(itemId);
    }

    setCompletedItems(newCompleted);

    // Сохраняем в localStorage
    localStorage.setItem(
      'completedItems',
      JSON.stringify(Array.from(newCompleted))
    );

    // Проверяем, все ли пункты раздела выполнены
    if (!roadmap) return;

    const chapterItems = roadmap.roadmapByChapter[chapter] || [];
    const allCompleted = chapterItems.every((item) =>
      newCompleted.has(item.id)
    );

    if (allCompleted && profile) {
      // Обновляем профиль в зависимости от раздела
      setUpdating(true);
      try {
        const updateData: any = {
          firstName: profile.firstName || '',
          lastName: profile.lastName || '',
          middleName: profile.middleName || '',
          countryOfArrival: profile.countryOfArrival || '',
          arrivalDate: profile.arrivalDate || '',
          hasRussianLanguageCertificate: profile.hasRussianLanguageCertificate,
          hasWorkPatent: profile.hasWorkPatent,
          hasPaidStateDuty: profile.hasPaidStateDuty,
        };

        if (chapter === 'Сертификат владения русским языком') {
          updateData.hasRussianLanguageCertificate = true;
        } else if (chapter === 'Патент на работу') {
          updateData.hasWorkPatent = true;
        } else if (chapter === 'Оплата госпошлины за патент') {
          updateData.hasPaidStateDuty = true;
        }

        const updatedProfile = await userService.updateProfile(updateData);
        setProfile(updatedProfile);

        // Перезагружаем дорожную карту, чтобы обновить список
        const updatedRoadmap = await roadmapService.getRoadmap();
        setRoadmap(updatedRoadmap);
      } catch (err: any) {
        console.error('Ошибка при обновлении профиля:', err);
        // Откатываем изменения
        setCompletedItems(completedItems);
      } finally {
        setUpdating(false);
      }
    }
  };

  if (loading) {
    return (
      <div className={styles.roadmap}>
        <div className={styles.container}>Загрузка...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.roadmap}>
        <div className={styles.container}>
          <div className={styles.error}>
            <p>{error}</p>
            <div className={styles.errorActions}>
              <button
                onClick={() => navigate('/')}
                className={styles.button}
                tabIndex={0}
                aria-label="Вернуться на главную"
              >
                На главную
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (!roadmap) {
    return null;
  }

  return (
    <div className={styles.roadmap}>
      <div className={styles.header}>
        <div className={styles.container}>
          <h1 className={styles.title}>Дорожная карта</h1>
          <div className={styles.actions}>
            <button
              onClick={handleGoToProfile}
              className={styles.button}
              tabIndex={0}
              aria-label="Редактировать профиль"
            >
              Редактировать профиль
            </button>
          </div>
        </div>
      </div>

      <div className={styles.container}>
        {!roadmap.isProfileComplete ? (
          <div className={styles.message}>
            <p>{roadmap.message}</p>
            <button
              onClick={handleGoToProfile}
              className={styles.button}
              tabIndex={0}
              aria-label="Заполнить профиль"
            >
              Заполнить профиль
            </button>
          </div>
        ) : (
          <>
            <div className={styles.message}>
              <p>{roadmap.message}</p>
            </div>

            {Object.keys(roadmap.roadmapByChapter).length === 0 ? (
              <div className={styles.success}>
                <h2>Поздравляем! 🎉</h2>
                <p>
                  Все необходимые документы оформлены. Вы выполнили все
                  требования.
                </p>
              </div>
            ) : (
              <div className={styles.roadmapTrack}>
                <div className={styles.startPoint}>
                  <div className={styles.startIcon}>🚀</div>
                  <div className={styles.startLabel}>СТАРТ</div>
                </div>

                <div className={styles.chapters}>
                  {Object.entries(roadmap.roadmapByChapter).map(
                    ([chapter, items]) => {
                      const allItemsCompleted = items.every((item) =>
                        completedItems.has(item.id)
                      );
                      return (
                        <div key={chapter} className={styles.chapterWrapper}>
                          <div
                            className={`${styles.chapter} ${
                              allItemsCompleted ? styles.chapterCompleted : ''
                            }`}
                          >
                            <h2 className={styles.chapterTitle}>
                              {chapter}
                              {allItemsCompleted && (
                                <span className={styles.completedBadge}>
                                  ✓ Выполнено
                                </span>
                              )}
                            </h2>
                            <ol className={styles.itemsList}>
                              {items.map((item) => {
                                const isCompleted = completedItems.has(item.id);
                                return (
                                  <li
                                    key={item.id}
                                    className={`${styles.item} ${
                                      isCompleted ? styles.itemCompleted : ''
                                    }`}
                                  >
                                    <label className={styles.itemLabel}>
                                      <input
                                        type="checkbox"
                                        checked={isCompleted}
                                        onChange={() =>
                                          handleItemToggle(item.id, chapter)
                                        }
                                        disabled={updating}
                                        className={styles.checkbox}
                                        tabIndex={0}
                                        aria-label={`Отметить как выполненное: ${item.name}`}
                                      />
                                      <span className={styles.itemText}>
                                        {item.name}
                                      </span>
                                    </label>
                                  </li>
                                );
                              })}
                            </ol>
                          </div>
                        </div>
                      );
                    }
                  )}
                </div>

                <div className={styles.finishPoint}>
                  <div className={styles.finishIcon}>🏁</div>
                  <div className={styles.finishLabel}>ФИНИШ</div>
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};
