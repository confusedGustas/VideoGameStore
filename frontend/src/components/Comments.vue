<template>
  <div class="comments-container bg-white shadow-md rounded-lg p-6 mt-8">
    <h2 class="text-2xl font-bold mb-4">Comments</h2>

    <div v-if="commentsError" class="text-gray-500">
      <p>Please login or create an account to view the comments.</p>
    </div>
    <form v-else @submit.prevent="addComment" class="mb-6">
      <textarea
        v-model="newComment"
        placeholder="Add a comment..."
        class="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-gray-200"
        rows="3"
      ></textarea>
      <Button type="submit" class="mt-2">Add Comment</Button>
    </form>

    <div v-if="!commentsError && comments.length > 0" class="space-y-4">
      <CommentItem
        v-for="comment in comments"
        :key="comment.commentId"
        :comment="comment"
        :gameId="gameId"
        @reply="replyToComment"
        @delete="deleteComment"
      />
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import axios from 'axios'
import Button from './Button.vue'
import CommentItem from './CommentItem.vue'

const props = defineProps({
  gameId: {
    type: Number,
    required: true
  }
})

const comments = ref([])
const newComment = ref('')
const commentsError = ref(false)

const fetchComments = async () => {
  try {
    const response = await axios.get(`/api/comment/get-comment/${props.gameId}`)
    comments.value = response.data.comments
    commentsError.value = false
  } catch (error) {
    console.error('Error fetching comments:', error)
    commentsError.value = true
  }
}

const addComment = async () => {
  if (!newComment.value.trim()) return

  try {
    await axios.post(`/api/comment/save-comment/${props.gameId}`, {
      gameId: props.gameId,
      content: newComment.value,
      parentCommentId: 0
    })
    newComment.value = ''
    await fetchComments()
  } catch (error) {
    console.error('Error adding comment:', error)
  }
}

const replyToComment = async (parentId, content) => {
  try {
    await axios.post(`/api/comment/save-comment/${props.gameId}`, {
      gameId: props.gameId,
      content: content,
      parentCommentId: parentId
    })
    await fetchComments()
  } catch (error) {
    console.error('Error replying to comment:', error)
  }
}

const deleteComment = async (commentId) => {
  try {
    await axios.post(`/api/comment/delete-comments/${commentId}`, {
      gameId: props.gameId
    })
    await fetchComments()
  } catch (error) {
    console.error('Error deleting comment:', error)
  }
}

onMounted(fetchComments)
</script>
