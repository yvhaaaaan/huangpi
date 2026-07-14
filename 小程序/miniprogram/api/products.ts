import { PageResult, request } from '../utils/request'

export interface ProductCategory {
  id: string
  code: string
  name: string
  priorityLevel: number
  sort: number
}

export interface ProductSummary {
  id: string
  categoryId: string
  categoryCode: string
  categoryName: string
  title: string
  summary: string
  coverUrl: string
  merchantName: string
  address?: string
  status: string
}

export interface ProductDetail extends ProductSummary {
  content: string
  imageUrls: string[]
  contactPhone?: string
  businessHours?: string
  latitude?: number
  longitude?: number
  publishedAt?: string
}

export interface ProductQuery {
  categoryCode?: string
  keyword?: string
  page?: number
  pageSize?: number
}

export const getProductCategories = (): Promise<ProductCategory[]> => request<ProductCategory[]>({ path: '/api/product-categories' })
export const getProducts = (query: ProductQuery = {}): Promise<PageResult<ProductSummary>> => request<PageResult<ProductSummary>>({ path: '/api/products', data: query })
export const getProductDetail = (id: string): Promise<ProductDetail> => request<ProductDetail>({ path: `/api/products/${encodeURIComponent(id)}` })

