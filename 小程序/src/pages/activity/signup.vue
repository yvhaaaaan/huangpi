<template>
  <view class="container page">
    <view class="card form">
      <input v-model="form.name" placeholder="??" />
      <input v-model="form.phone" placeholder="???" />
      <input v-model="form.count" placeholder="????" />
      <textarea v-model="form.remark" placeholder="??"></textarea>
      <button class="btn" @click="submit">????</button>
    </view>
  </view>
</template>
<script setup>
import { reactive } from 'vue'
const form = reactive({ name: '', phone: '', count: '1', remark: '' })
const submit = () => uni.showToast({ title: '?????', icon: 'success' })
</script>
<style lang="scss">@import '@/styles/theme.scss';.page{padding:24rpx}.form{padding:24rpx;display:grid;gap:18rpx}input,textarea{background:#f3f7f4;padding:18rpx 20rpx;border-radius:16rpx}.btn{background:$primary;color:#fff;border-radius:999rpx}</style>