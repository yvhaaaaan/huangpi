import { PageResult, request } from '../utils/request'

export type ReviewStatus = 'pending' | 'approved' | 'rejected' | 'closed'

export interface AdminDashboard {
  pending: number
  approvedToday: number
  merchants: number
  publishedProducts: number
}

export interface AdminReviewItem {
  id: string
  targetType: 'merchant_profile' | 'merchant_product'
  targetId: string
  merchantId: string
  title: string
  owner: string
  categoryName?: string
  summary: string
  coverUrl?: string
  status: ReviewStatus
  submittedAt: string
  reviewComment?: string
}

export interface AdminReviewQuery {
  status?: ReviewStatus | 'all'
  targetType?: string
  page?: number
  pageSize?: number
}

export interface AdminCategoryPayload {
  code: string
  name: string
  priorityLevel: number
  sort: number
  status: 'enabled' | 'disabled'
}

export const getAdminDashboard = (): Promise<AdminDashboard> => request<AdminDashboard>({ path: '/api/admin/dashboard' })
export const getAdminReviews = (query: AdminReviewQuery = {}): Promise<PageResult<AdminReviewItem>> => request<PageResult<AdminReviewItem>>({ path: '/api/admin/reviews', data: query })
export const getAdminReview = (id: string): Promise<AdminReviewItem> => request<AdminReviewItem>({ path: `/api/admin/reviews/${encodeURIComponent(id)}` })
export const approveAdminReview = (id: string, comment = ''): Promise<void> => request<void>({ path: `/api/admin/reviews/${encodeURIComponent(id)}/approve`, method: 'POST', data: { comment } })
export const rejectAdminReview = (id: string, reason: string): Promise<void> => request<void>({ path: `/api/admin/reviews/${encodeURIComponent(id)}/reject`, method: 'POST', data: { reason } })
export const closeAdminReview = (id: string, reason = ''): Promise<void> => request<void>({ path: `/api/admin/reviews/${encodeURIComponent(id)}/close`, method: 'POST', data: { reason } })
export const getAdminMerchants = (page = 1): Promise<PageResult<Record<string, unknown>>> => request<PageResult<Record<string, unknown>>>({ path: '/api/admin/merchants', data: { page } })
export const getAdminCategories = (): Promise<Array<AdminCategoryPayload & { id: string }>> => request<Array<AdminCategoryPayload & { id: string }>>({ path: '/api/admin/product-categories' })
export const createAdminCategory = (payload: AdminCategoryPayload): Promise<{ id: string }> => request<{ id: string }>({ path: '/api/admin/product-categories', method: 'POST', data: payload })
export const updateAdminCategory = (id: string, payload: Partial<AdminCategoryPayload>): Promise<void> => request<void>({ path: `/api/admin/product-categories/${encodeURIComponent(id)}`, method: 'PUT', data: payload })
export const getAuditLogs = (page = 1): Promise<PageResult<Record<string, unknown>>> => request<PageResult<Record<string, unknown>>>({ path: '/api/admin/audit-logs', data: { page } })
