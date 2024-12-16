<template>
  <div class="comment-item bg-gray-50 px-4 pb-1.5 pt-1.5 rounded-lg">
    <div class="flex justify-between items-start">
      <div>
        <p class="font-semibold">{{ comment.username }}</p>
        <p class="text-sm text-gray-500">{{ formatDate(comment.createdAt) }}</p>
      </div>
      <Button @click="deleteComment" variant="secondary" class="text-red-500">
        <TrashIcon class="h-4 w-4" />
      </Button>
    </div>
    <p class="mt-2">{{ comment.comment }}</p>

    <div v-if="showReplyForm" class="mt-4">
      <textarea
        v-model="replyText"
        placeholder="Write a reply..."
        class="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gray-200"
        rows="2"
      ></textarea>
      <div class="mt-2 space-x-2">
        <Button @click="submitReply">Reply</Button>
        <Button @click="showReplyForm = false" variant="secondary">Cancel</Button>
      </div>
    </div>
    <Button v-else @click="showReplyForm = true" variant="secondary" class="mt-2">
      Reply
    </Button>

    <div v-if="comment.replies && comment.replies.length > 0" class="border-l-2 border-gray-200">
      <CommentItem
        v-for="reply in comment.replies"
        :key="reply.commentId"
        :comment="reply"
        :gameId="gameId"
        @reply="(parentId, content) => $emit('reply', parentId, content)"
        @delete="$emit('delete', reply.commentId)"
        class="whitespace-pre-wrap break-words"
      />
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import Button from './Button.vue'
import {TrashIcon} from 'lucide-vue-next'

const props = defineProps({
  comment: {
    type: Object,
    required: true
  },
  gameId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['reply', 'delete'])

const showReplyForm = ref(false)
const replyText = ref('')

const submitReply = () => {
  if (replyText.value.trim()) {
    emit('reply', props.comment.commentId, replyText.value)
    replyText.value = ''
    showReplyForm.value = false
  }
}

const deleteComment = () => {
  emit('delete', props.comment.commentId)
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString()
}
</script>
