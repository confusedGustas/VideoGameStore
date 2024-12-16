<template>
  <div class="comment-item bg-gray-50 px-3 py-2 rounded-md">
    <div class="flex justify-between items-center">
      <div class="text-sm">
        <p class="font-medium">{{ comment.username }}</p>
        <p class="text-xs text-gray-400">{{ formatDate(comment.createdAt) }}</p>
      </div>
      <Button
        v-if="canDelete"
        @click="deleteComment"
        variant="secondary"
        class="text-red-500 text-xs"
      >
        <TrashIcon class="h-3 w-3" />
      </Button>
    </div>

    <p class="mt-1 text-sm">{{ comment.comment }}</p>

    <div v-if="showReplyForm" class="mt-2">
      <textarea
        v-model="replyText"
        placeholder="Write a reply..."
        class="w-full p-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring focus:ring-gray-200"
        rows="1"
      ></textarea>
      <div class="mt-1 space-x-1">
        <Button @click="submitReply" class="text-sm">Reply</Button>
        <Button @click="showReplyForm = false" variant="secondary" class="text-sm">Cancel</Button>
      </div>
    </div>

    <Button v-else @click="showReplyForm = true" variant="secondary" class="mt-1 text-xs">
      Reply
    </Button>

    <div v-if="comment.replies && comment.replies.length > 0" class="pl-3 border-l-2 border-gray-200 mt-2">
      <CommentItem
        v-for="reply in comment.replies"
        :key="reply.commentId"
        :comment="reply"
        :gameId="gameId"
        :loggedInUsername="loggedInUsername"
        :loggedInRole="loggedInRole"
        @reply="(parentId, content) => $emit('reply', parentId, content)"
        @delete="$emit('delete', reply.commentId)"
        class="whitespace-pre-wrap break-words"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import Button from './Button.vue';
import { TrashIcon } from 'lucide-vue-next';

const props = defineProps({
  comment: { type: Object, required: true },
  gameId: { type: Number, required: true },
  loggedInUsername: { type: String, required: true },
  loggedInRole: { type: String, required: true },
});

const emit = defineEmits(['reply', 'delete']);
const showReplyForm = ref(false);
const replyText = ref('');

const canDelete = computed(() => {
  return props.loggedInRole === 'ADMIN' || props.comment.username === props.loggedInUsername;
});

const submitReply = () => {
  if (replyText.value.trim()) {
    emit('reply', props.comment.commentId, replyText.value);
    replyText.value = '';
    showReplyForm.value = false;
  }
};

const deleteComment = () => {
  emit('delete', props.comment.commentId);
};

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString();
};
</script>
