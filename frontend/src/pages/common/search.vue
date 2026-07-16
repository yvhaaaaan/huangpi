<template>
  <view class="container page">
    <view class="search-box card">
      <input v-model="keyword" placeholder="搜索油茶、特产、文旅、非遗、活动" />
      <button class="btn" @click="search">搜索</button>
    </view>
    <view class="card history" v-if="history.length">
      <view class="section-title"><text>历史搜索</text></view>
      <view class="tag-row">
        <text class="tag" v-for="h in history" :key="h" @click="keyword = h">{{ h }}</text>
      </view>
    </view>
  </view>
</template>
<script setup>
import { ref } from 'vue'
const keyword = ref('')
const history = ['油茶', '特产', '非遗']
const search = () => uni.showToast({ title: keyword.value || '请输入关键词', icon: 'none' })
</script>
<style lang="scss">
@import '@/styles/theme.scss';
.page{padding:24rpx}.search-box{padding:20rpx;display:flex;gap:16rpx}input{flex:1;background:#f3f7f4;border-radius:999rpx;padding:18rpx 24rpx}.btn{background:$primary;color:#fff;border-radius:999rpx}.history{padding:22rpx;margin-top:18rpx}.tag-row{display:flex;flex-wrap:wrap;gap:12rpx}
</style>
