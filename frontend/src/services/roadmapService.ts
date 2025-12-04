import api from './api';
import { RoadmapResponse } from '../types';

export const roadmapService = {
  getRoadmap: async (): Promise<RoadmapResponse> => {
    const response = await api.get<RoadmapResponse>('/roadmap');
    return response.data;
  },
};
