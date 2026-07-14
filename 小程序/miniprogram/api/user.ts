import { PageResult, request } from '../utils/request'

export interface CurrentUser {
  id: string
  nickname: string
  avatarUrl?: string
  role: 'user' | 'merchant' | 'admin'
}

export interface HomeData {
  banners: Array<Record<string, unknown>>
  featuredCategories: Array<Record<string, unknown>>
  recommendedProducts: Array<Record<string, unknown>>
  recommendedMapPoints: Array<Record<string, unknown>>
  activities: Array<Record<string, unknown>>
}

export interface ActivitySignupPayload {
  name: string
  phone: string
  peopleCount: number
  remark?: string
}

export const getCurrentUser = (): Promise<CurrentUser> => request<CurrentUser>({ path: '/api/me' })
export const getHomeData = (): Promise<HomeData> => request<HomeData>({ path: '/api/home' })
export const searchContents = (keyword: string, type = '', page = 1): Promise<PageResult<Record<string, unknown>>> => request<PageResult<Record<string, unknown>>>({ path: '/api/search', data: { keyword, type, page } })
export const getMapPoints = (): Promise<Array<Record<string, unknown>>> => request<Array<Record<string, unknown>>>({ path: '/api/map/points' })
export const getRoutes = (): Promise<Array<Record<string, unknown>>> => request<Array<Record<string, unknown>>>({ path: '/api/routes' })
export const getActivities = (page = 1): Promise<PageResult<Record<string, unknown>>> => request<PageResult<Record<string, unknown>>>({ path: '/api/activities', data: { page } })
export const getActivityDetail = (id: string): Promise<Record<string, unknown>> => request<Record<string, unknown>>({ path: `/api/activities/${encodeURIComponent(id)}` })
export const signupActivity = (id: string, payload: ActivitySignupPayload): Promise<{ id: string }> => request<{ id: string }>({ path: `/api/activities/${encodeURIComponent(id)}/signups`, method: 'POST', data: payload })
export const getMySignups = (): Promise<Array<Record<string, unknown>>> => request<Array<Record<string, unknown>>>({ path: '/api/me/signups' })
export const getMyFavorites = (): Promise<Array<Record<string, unknown>>> => request<Array<Record<string, unknown>>>({ path: '/api/me/favorites' })
export const addFavorite = (targetType: string, targetId: string): Promise<{ id: string }> => request<{ id: string }>({ path: '/api/favorites', method: 'POST', data: { targetType, targetId } })
export const removeFavorite = (id: string): Promise<void> => request<void>({ path: `/api/favorites/${encodeURIComponent(id)}`, method: 'DELETE' })

