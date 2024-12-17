<template>
  <div class="comment-item bg-gray-50 px-2 py-1 rounded-md">
    <div class="flex justify-between items-center">
      <div class="text-sm">
        <p class="font-medium">{{ comment.username }}</p>
      </div>
    </div>

    <p class="mt-1 text-sm">{{ comment.comment }}</p>
    <p class="mt-1 text-xs text-gray-500">{{ comment.createdAt }}</p>

    <div v-if="showReplyForm" class="mt-2">
      <textarea
        v-model="replyContent"
        placeholder="Write a reply..."
        class="w-full p-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring focus:ring-gray-200"
        rows="1"
      ></textarea>
      <div class="mt-1 space-x-1">
        <Button @click="submitReply" class="text-xs">Reply</Button>
        <Button @click="cancelReply" variant="secondary" class="text-xs">Cancel</Button>
      </div>
    </div>

    <div v-else class="mt-1 space-x-1">
      <Button @click="showReplyForm = true" variant="secondary" class="text-xs">
        Reply
      </Button>
      <Button
        v-if="canDelete"
        @click="deleteComment"
        variant="secondary"
        class="text-red-500 text-xs pb-3"
      >
      <TrashIcon class="h-3 w-3" />
      </Button>
    </div>

    <div v-if="comment.replies && comment.replies.length > 0" class="pl-3 border-l-2 border-gray-200 mt-2">
      <CommentItem
        v-for="reply in comment.replies"
        :key="reply.commentId"
        :comment="reply"
        :gameId="gameId"
        :loggedInUsername="loggedInUsername"
        :loggedInRole="loggedInRole"
        @reply="(parentId, content) => $emit('reply', parentId, content)"
        @delete="$emit('delete', $event)"
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
  comment: {
    type: Object,
    required: true
  },
  gameId: {
    type: Number,
    required: true
  },
  loggedInUsername: {
    type: String,
    required: true
  },
  loggedInRole: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['reply', 'delete']);

const showReplyForm = ref(false);
const replyContent = ref('');

const canDelete = computed(() => {
  return props.loggedInRole === 'ADMIN' || props.comment.username === props.loggedInUsername;
});

const submitReply = () => {
  if (replyContent.value.trim()) {
    emit('reply', props.comment.commentId, replyContent.value);
    replyContent.value = '';
    showReplyForm.value = false;
  }
};

const cancelReply = () => {
  showReplyForm.value = false;
  replyContent.value = '';
};

const deleteComment = () => {
  emit('delete', props.comment.commentId);
};
</script>
