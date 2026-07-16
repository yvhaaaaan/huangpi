<template>
  <view class="container page">
    <view class="map card">
      <image class="map-image" src="/static/map-point.jpg" mode="aspectFill" />
      <view class="map-float">地图导览区域</view>
    </view>
    <view class="list card" v-for="item in points" :key="item.id">
      <text class="title">{{ item.title }}</text>
      <text class="desc">{{ item.desc }}</text>
      <view class="actions"><text class="tag">查看详情</text><text class="tag">一键导航</text></view>
    </view>
  </view>
</template>
<script setup>
const points = [
  { id: 1, title: '油茶体验点', desc: '油茶制作与品鉴体验' },
  { id: 2, title: '客家古村落', desc: '客家建筑与乡土文化' },
  { id: 3, title: '非遗工坊', desc: '传统技艺体验空间' }
]
</script>
<style lang="scss">
@import '@/styles/theme.scss';
.page{padding:24rpx}.map{height:360rpx;position:relative;overflow:hidden}.map-image{width:100%;height:100%}.map-float{position:absolute;left:22rpx;top:22rpx;padding:10rpx 16rpx;border-radius:999rpx;background:rgba(255,255,255,.92);color:$primary;font-size:24rpx}.list{padding:20rpx;margin-top:18rpx}.title{font-weight:700;display:block;margin-bottom:8rpx}.desc{color:$text-secondary;font-size:24rpx}.actions{margin-top:14rpx;display:flex;gap:12rpx}.tag{padding:8rpx 16rpx;border-radius:999rpx;background:$primary-light;color:$primary;font-size:22rpx}
</style>