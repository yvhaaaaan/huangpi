import { PageResult, request } from '../utils/request'

export interface MerchantDashboard {
  stats: { published: number; pending: number; draft: number }
  messages: MerchantMessage[]
  todos: MerchantTodo[]
}

export interface MerchantMessage {
  id: string
  type: string
  title: string
  content: string
  readAt?: string
  createdAt: string
}

export interface MerchantTodo {
  id: string
  type: string
  title: string
  description: string
  productId?: string
  level: string
}

export interface MerchantProfilePayload {
  name: string
  owner?: string
  phone: string
  address: string
  businessHours?: string
  intro?: string
  coverFileId?: string
}

export interface MerchantProductPayload {
  title: string
  categoryId: string
  summary: string
  content?: string
  coverFileId: string
  imageFileIds: string[]
  address?: string
  contactPhone?: string
  businessHours?: string
  latitude?: number
  longitude?: number
}

export const getMerchantDashboard = (): Promise<MerchantDashboard> => request<MerchantDashboard>({ path: '/api/merchant/dashboard' })
export const getMerchantProfile = (): Promise<Record<string, unknown>> => request<Record<string, unknown>>({ path: '/api/merchant/profile' })
export const updateMerchantProfile = (payload: Partial<MerchantProfilePayload>): Promise<void> => request<void>({ path: '/api/merchant/profile', method: 'PUT', data: payload })
export const submitMerchantProfile = (): Promise<void> => request<void>({ path: '/api/merchant/profile/submit', method: 'POST' })
export const getMerchantProducts = (page = 1): Promise<PageResult<Record<string, unknown>>> => request<PageResult<Record<string, unknown>>>({ path: '/api/merchant/products', data: { page } })
export const getMerchantProduct = (id: string): Promise<Record<string, unknown>> => request<Record<string, unknown>>({ path: `/api/merchant/products/${encodeURIComponent(id)}` })
export const createMerchantProduct = (payload: MerchantProductPayload): Promise<{ id: string }> => request<{ id: string }>({ path: '/api/merchant/products', method: 'POST', data: payload })
export const updateMerchantProduct = (id: string, payload: Partial<MerchantProductPayload>): Promise<void> => request<void>({ path: `/api/merchant/products/${encodeURIComponent(id)}`, method: 'PUT', data: payload })
export const submitMerchantProduct = (id: string): Promise<void> => request<void>({ path: `/api/merchant/products/${encodeURIComponent(id)}/submit`, method: 'POST' })
export const closeMerchantProduct = (id: string): Promise<void> => request<void>({ path: `/api/merchant/products/${encodeURIComponent(id)}/close`, method: 'POST' })
export const getMerchantMessages = (): Promise<MerchantMessage[]> => request<MerchantMessage[]>({ path: '/api/merchant/messages' })
export const getMerchantTodos = (): Promise<MerchantTodo[]> => request<MerchantTodo[]>({ path: '/api/merchant/todos' })
