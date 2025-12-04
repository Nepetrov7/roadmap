import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { userService } from '../../services/userService';
import { UserProfileResponse, UserProfileUpdateRequest } from '../../types';
import styles from './Profile.module.scss';

export const Profile: React.FC = () => {
  const navigate = useNavigate();
  const [_, setProfile] = useState<UserProfileResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [saving, setSaving] = useState<boolean>(false);
  const [error, setError] = useState<string>('');
  const [formData, setFormData] = useState<UserProfileUpdateRequest>({
    firstName: '',
    lastName: '',
    middleName: '',
    countryOfArrival: '',
    arrivalDate: '',
    hasRussianLanguageCertificate: false,
    hasWorkPatent: false,
    hasPaidStateDuty: false,
  });

  useEffect(() => {
    const loadProfile = async (): Promise<void> => {
      try {
        const data = await userService.getProfile();
        setProfile(data);
        setFormData({
          firstName: data.firstName || '',
          lastName: data.lastName || '',
          middleName: data.middleName || '',
          countryOfArrival: data.countryOfArrival || '',
          arrivalDate: data.arrivalDate || '',
          hasRussianLanguageCertificate: data.hasRussianLanguageCertificate,
          hasWorkPatent: data.hasWorkPatent,
          hasPaidStateDuty: data.hasPaidStateDuty,
        });
      } catch (err: any) {
        setError('Ошибка при загрузке профиля');
      } finally {
        setLoading(false);
      }
    };

    loadProfile();
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
    setError('');
  };

  const handleSubmit = async (
    e: React.FormEvent<HTMLFormElement>
  ): Promise<void> => {
    e.preventDefault();
    setError('');
    setSaving(true);

    try {
      const updated = await userService.updateProfile(formData);
      setProfile(updated);
      navigate('/roadmap');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Ошибка при сохранении профиля');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className={styles.profile}>
        <div className={styles.container}>Загрузка...</div>
      </div>
    );
  }

  return (
    <div className={styles.profile}>
      <div className={styles.container}>
        <h1 className={styles.title}>Заполните данные профиля</h1>

        <form onSubmit={handleSubmit} className={styles.form}>
          {error && <div className={styles.error}>{error}</div>}

          <div className={styles.field}>
            <label htmlFor="lastName" className={styles.label}>
              Фамилия
            </label>
            <input
              type="text"
              id="lastName"
              name="lastName"
              value={formData.lastName}
              onChange={handleChange}
              className={styles.input}
              required
              aria-label="Фамилия"
              tabIndex={0}
            />
          </div>

          <div className={styles.field}>
            <label htmlFor="firstName" className={styles.label}>
              Имя
            </label>
            <input
              type="text"
              id="firstName"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              className={styles.input}
              required
              aria-label="Имя"
              tabIndex={0}
            />
          </div>

          <div className={styles.field}>
            <label htmlFor="middleName" className={styles.label}>
              Отчество
            </label>
            <input
              type="text"
              id="middleName"
              name="middleName"
              value={formData.middleName}
              onChange={handleChange}
              className={styles.input}
              required
              aria-label="Отчество"
              tabIndex={0}
            />
          </div>

          <div className={styles.field}>
            <label htmlFor="countryOfArrival" className={styles.label}>
              Страна, откуда прибыл
            </label>
            <input
              type="text"
              id="countryOfArrival"
              name="countryOfArrival"
              value={formData.countryOfArrival}
              onChange={handleChange}
              className={styles.input}
              required
              aria-label="Страна прибытия"
              tabIndex={0}
            />
          </div>

          <div className={styles.field}>
            <label htmlFor="arrivalDate" className={styles.label}>
              Дата прибытия
            </label>
            <input
              type="date"
              id="arrivalDate"
              name="arrivalDate"
              value={formData.arrivalDate}
              onChange={handleChange}
              className={styles.input}
              required
              aria-label="Дата прибытия"
              tabIndex={0}
            />
          </div>

          <div className={styles.checkboxField}>
            <label className={styles.checkboxLabel}>
              <input
                type="checkbox"
                name="hasRussianLanguageCertificate"
                checked={formData.hasRussianLanguageCertificate}
                onChange={handleChange}
                tabIndex={0}
                aria-label="Есть сертификат владения русским языком"
              />
              <span>Есть сертификат владения русским языком</span>
            </label>
          </div>

          <div className={styles.checkboxField}>
            <label className={styles.checkboxLabel}>
              <input
                type="checkbox"
                name="hasWorkPatent"
                checked={formData.hasWorkPatent}
                onChange={handleChange}
                tabIndex={0}
                aria-label="Есть патент на работу"
              />
              <span>Есть патент на работу</span>
            </label>
          </div>

          {formData.hasWorkPatent && (
            <div className={styles.checkboxField}>
              <label className={styles.checkboxLabel}>
                <input
                  type="checkbox"
                  name="hasPaidStateDuty"
                  checked={formData.hasPaidStateDuty}
                  onChange={handleChange}
                  tabIndex={0}
                  aria-label="Оплачена госпошлина за патент"
                />
                <span>Оплачена госпошлина за патент</span>
              </label>
            </div>
          )}

          <button
            type="submit"
            className={styles.button}
            disabled={saving}
            tabIndex={0}
            aria-label="Сохранить профиль"
          >
            {saving ? 'Сохранение...' : 'Сохранить и перейти к дорожной карте'}
          </button>
        </form>
      </div>
    </div>
  );
};
