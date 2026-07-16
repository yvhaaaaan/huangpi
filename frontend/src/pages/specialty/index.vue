<template>
  <view class="container page">
    <view class="filter-bar card">
      <scroll-view scroll-x class="tabs">
        <view v-for="t in tabs" :key="t" class="tab" :class="{ active: current === t }" @click="current = t">{{ t }}</view>
      </scroll-view>
    </view>

    <view class="spec-card card" v-for="item in list" :key="item.id" @click="detail(item)">
      <image class="spec-image" :src="item.image" mode="aspectFill" />
      <view class="spec-body">
        <text class="spec-title">{{ item.title }}</text>
        <text class="spec-desc">{{ item.desc }}</text>
        <view class="spec-meta">
          <text class="tag">{{ item.tag }}</text>
          <text class="muted">{{ item.shop }}</text>
        </view>
      </view>
    </view>
  </view>
</template>
<script setup>
import { computed, ref } from 'vue'
const current = ref('??')
const tabs = ['??', '??', '??', '????']
const all = [
  { id: 1, title: '??????', tag: '??', shop: '????????', desc: '?????????????', image: '/static/sp1.jpg' },
  { id: 2, title: '??????', tag: '??', shop: '?????', desc: '?????????????', image: '/static/sp2.jpg' },
  { id: 3, title: '???????', tag: '????', shop: '?????', desc: '???????????', image: '/static/sp3.jpg' }
]
const list = computed(() => current.value === '??' ? all : all.filter(i => i.tag === current.value || (current.value === '????' && i.tag.includes('??'))))
const detail = (item) => uni.showToast({ title: item.title, icon: 'none' })
</script>
<style lang="scss">
@import '@/styles/theme.scss';
.page{padding:24rpx}.filter-bar{padding:18rpx}.tabs{white-space:nowrap}.tab{display:inline-block;padding:14rpx 22rpx;margin-right:16rpx;border-radius:999rpx;background:#f3f7f4;color:$text-secondary}.tab.active{background:$primary;color:#fff}.spec-card{display:flex;overflow:hidden;margin-top:18rpx}.spec-image{width:220rpx;height:180rpx;background:#eef3ef}.spec-body{flex:1;padding:18rpx}.spec-title{font-size:32rpx;font-weight:700;display:block;margin-bottom:8rpx}.spec-desc{color:$text-secondary;font-size:24rpx;line-height:1.5}.spec-meta{margin-top:14rpx;display:flex;justify-content:space-between;align-items:center;gap:12rpx}
</style>