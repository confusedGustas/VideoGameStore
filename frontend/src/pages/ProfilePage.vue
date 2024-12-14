<template>
  <div>
    <nav class="navbar">
      <div class="navbar-left">
        <button @click="showChangeUsernameModal = true">Change Username</button>
        <button @click="showChangeEmailModal = true">Change Email</button>
        <button @click="showChangePasswordModal = true">Change Password</button>
      </div>
      <router-link to="/profile" class="navbar-title" exact-active-class="active-link">Profile</router-link>
      <div class="navbar-buttons">
        <button @click="goHome">Home</button>
        <button v-if="isAdmin" @click="goToAdminPanel">Admin Panel</button>
        <button v-if="userLoggedIn" @click="logout" class="logout">
          Logout
        </button>
      </div>
    </nav>

    <transition name="fade">
      <div v-if="showChangeUsernameModal" class="modal-overlay">
        <div class="modal-content">
          <h2>Change Username</h2>
          <form @submit.prevent="updateUsername">
            <label for="newUsername">New Username:</label>
            <input v-model="newUsername" type="text" id="newUsername" required />
            <div class="modal-actions">
              <button type="submit">Save</button>
              <button @click="showChangeUsernameModal = false" type="button">
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <transition name="fade">
      <div v-if="showChangeEmailModal" class="modal-overlay">
        <div class="modal-content">
          <h2>Change Email</h2>
          <form @submit.prevent="updateEmail">
            <label for="newEmail">New Email:</label>
            <input v-model="newEmail" type="email" id="newEmail" required />
            <div class="modal-actions">
              <button type="submit">Save</button>
              <button @click="showChangeEmailModal = false" type="button">
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <transition name="fade">
      <div v-if="showChangePasswordModal" class="modal-overlay">
        <div class="modal-content">
          <h2>Change Password</h2>
          <form @submit.prevent="updatePassword">
            <label for="newPassword">New Password:</label>
            <input v-model="newPassword" type="password" id="newPassword" required />
            <div class="modal-actions">
              <button type="submit">Save</button>
              <button @click="showChangePasswordModal = false" type="button">
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <div v-if="userDetails">
      <button class="add-game-button" @click="showAddGamePopup = true">
        Add Game
      </button>

      <div v-if="games.length > 0">
        <div class="game-grid">
          <div v-for="game in games" :key="game.id" class="game-card">
            <img :src="getImageUrl(game.id)" :alt="game.name" />
            <h3 class="game-title">{{ game.name }}</h3>
            <div class="game-actions">
              <button @click="goToDetails(game.id)" class="details-button">
                Details
              </button>
              <button @click="editGame(game.id)" class="edit-button">
                Edit
              </button>
              <button @click="deleteGame(game.id)" class="delete-button">
                Delete
              </button>
            </div>
          </div>
        </div>

        <div class="pagination-controls">
          <button @click="prevPage" :disabled="currentPage === 1">
            Previous
          </button>
          <span>Page {{ currentPage }}</span>
          <button @click="nextPage" :disabled="!hasMoreGames">Next</button>
        </div>
      </div>

      <div v-else class="no-listings-container">
        <p class="no-listings-message">No listings available</p>
      </div>
    </div>

    <div v-else>
      <p>Redirecting to login...</p>
    </div>

    <transition name="fade">
      <div v-if="showAddGamePopup" class="modal-overlay">
        <div class="modal-content">
          <h2>Add New Game</h2>
          <form @submit.prevent="saveGame">
            <label for="name">Name:</label>
            <input v-model="newGame.name" type="text" id="name" required />

            <label for="price">Price:</label>
            <input
              v-model="newGame.price"
              type="text"
              id="price"
              pattern="^\d*(\.\d{0,2})?$"
              inputmode="decimal"
              step="0.01"
              required
              @input="validatePrice($event, 'newGame')"
            />

            <label for="description">Description:</label>
            <textarea
              v-model="newGame.description"
              id="description"
              maxlength="200"
              required
            ></textarea>

            <label for="releaseYear">Release Year:</label>
            <input
              v-model="newGame.releaseYear"
              type="number"
              id="releaseYear"
              required
            />

            <label for="region">Region:</label>
            <input v-model="newGame.region" type="text" id="region" required />

            <label for="platform">Platform:</label>
            <input v-model="newGame.platform" type="text" id="platform" required />

            <label for="publisher">Publisher:</label>
            <input v-model="newGame.publisher" type="text" id="publisher" required />

            <label for="genre">Genre:</label>
            <input v-model="newGame.genre" type="text" id="genre" required />

            <label for="stock">Stock:</label>
            <input v-model="newGame.stock" type="number" id="stock" required />

            <label for="image">Image:</label>
            <input type="file" id="image" @change="handleImageUpload" />

            <div class="modal-actions">
              <button type="submit">Save</button>
              <button @click="showAddGamePopup = false" type="button">
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>

    <transition name="fade">
      <div v-if="showEditGamePopup" class="modal-overlay">
        <div class="modal-content">
          <h2>Edit Game</h2>
          <form @submit.prevent="updateGame">
            <label for="editName">Name:</label>
            <input v-model="editingGame.name" type="text" id="editName" required />

            <label for="editPrice">Price:</label>
            <input
              v-model="editingGame.price"
              type="text"
              id="editPrice"
              pattern="^\d*(\.\d{0,2})?$"
              inputmode="decimal"
              step="0.01"
              required
              @input="validatePrice($event, 'editingGame')"
            />

            <label for="editDescription">Description:</label>
            <textarea
              v-model="editingGame.description"
              id="editDescription"
              maxlength="200"
              required
            ></textarea>

            <label for="editReleaseYear">Release Year:</label>
            <input
              v-model="editingGame.releaseYear"
              type="number"
              id="editReleaseYear"
              required
            />

            <label for="editRegion">Region:</label>
            <input v-model="editingGame.activationRegion.regionName" type="text" id="editRegion" required />

            <label for="editPlatform">Platform:</label>
            <input v-model="editingGame.activationPlatform.platformName" type="text" id="editPlatform" required />

            <label for="editPublisher">Publisher:</label>
            <input v-model="editingGame.publisher.publisherName" type="text" id="editPublisher" required />

            <label for="editGenre">Genre:</label>
            <input v-model="editingGame.genre.name" type="text" id="editGenre" required />

            <label for="editStock">Stock:</label>
            <input v-model="editingGame.stock" type="number" id="editStock" required />

            <label for="editImage">Image:</label>
            <input
              type="file"
              id="editImage"
              @change="handleEditImageUpload"
              ref="editImageInput"
            />

            <div class="modal-actions">
              <button type="submit">Update</button>
              <button @click="showEditGamePopup = false" type="button">
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, nextTick } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const userDetails = ref(null);
const games = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;
const hasMoreGames = ref(true);
const userLoggedIn = ref(false);
const showAddGamePopup = ref(false);
const showEditGamePopup = ref(false);
const newGame = ref({
  name: '',
  price: null,
  description: '',
  image: null,
  releaseYear: null,
  region: '',
  platform: '',
  publisher: '',
  genre: '',
  stock: null,
  imageFile: null,
});
const editingGame = ref({
  id: null,
  name: '',
  price: null,
  description: '',
  releaseYear: null,
  stock: null,
  activationRegion: { id: null, regionName: '' },
  activationPlatform: { id: null, platformName: '' },
  publisher: { id: null, publisherName: '' },
  genre: { id: null, name: '' },
  seller: '',
  imageFile: null,
  imagePreview: null,
});
const editImageInput = ref(null);
const showChangeUsernameModal = ref(false);
const showChangeEmailModal = ref(false);
const showChangePasswordModal = ref(false);
const newUsername = ref('');
const newEmail = ref('');
const newPassword = ref('');
const isAdmin = ref(false);

const generateUniqueFilename = (originalName) => {
  const now = new Date();
  const timestamp = `${now.getFullYear()}${(now.getMonth() + 1)
    .toString()
    .padStart(2, '0')}${now
    .getDate()
    .toString()
    .padStart(2, '0')}${now
    .getHours()
    .toString()
    .padStart(2, '0')}${now
    .getMinutes()
    .toString()
    .padStart(2, '0')}${now.getSeconds().toString().padStart(2, '0')}`;
  const extension = originalName.split('.').pop();
  return `${timestamp}.${extension}`;
};

const goToAdminPanel = () => {
  router.push({ name: 'admin' });
};

const handleImageUpload = (event) => {
  const files = event.target.files;
  if (files && files.length > 0) {
    const lastFile = files[files.length - 1];
    newGame.value.image = generateUniqueFilename(lastFile.name);
    newGame.value.imageFile = lastFile;
  }
};

const updateUsername = async () => {
  try {
    await axios.post('/api/users/update-username', { username: newUsername.value });
    showChangeUsernameModal.value = false;
    newUsername.value = '';
    router.push({ name: 'home' });
  } catch (error) {
    console.error('Error updating username:', error);
  }
};

const updateEmail = async () => {
  try {
    await axios.post('/api/users/update-email', { email: newEmail.value });
    showChangeEmailModal.value = false;
    newEmail.value = '';
    router.push({ name: 'home' });
  } catch (error) {
    console.error('Error updating email:', error);
  }
};

const updatePassword = async () => {
  try {
    await axios.post('/api/users/update-password', { password: newPassword.value });
    showChangePasswordModal.value = false;
    newPassword.value = '';
    router.push({ name: 'home' });
  } catch (error) {
    console.error('Error updating password:', error);
  }
};

const handleEditImageUpload = (event) => {
  const files = event.target.files;
  if (files && files.length > 0) {
    const lastFile = files[files.length - 1];
    editingGame.value.imageFile = lastFile;
    editingGame.value.imagePreview = URL.createObjectURL(lastFile);
  }
};

const saveGame = async () => {
  try {
    const formData = new FormData();
    formData.append('name', newGame.value.name);
    formData.append('price', parseFloat(newGame.value.price));
    formData.append('description', newGame.value.description);
    formData.append('releaseYear', newGame.value.releaseYear);
    formData.append('region', newGame.value.region);
    formData.append('platform', newGame.value.platform);
    formData.append('publisher', newGame.value.publisher);
    formData.append('genre', newGame.value.genre);
    formData.append('stock', newGame.value.stock);

    if (newGame.value.imageFile) {
      formData.append(
        'image',
        new File([newGame.value.imageFile], newGame.value.image)
      );
    }

    await axios.post('/api/games/save', formData, {
      withCredentials: true,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    newGame.value = {
      name: '',
      price: null,
      description: '',
      image: null,
      releaseYear: null,
      region: '',
      platform: '',
      publisher: '',
      genre: '',
      stock: null,
      imageFile: null,
    };

    document.querySelector('input[type="file"]').value = '';

    showAddGamePopup.value = false;
    await fetchGames(currentPage.value);
  } catch (error) {
    console.error('Error saving the game:', error);
  }
};

const checkUserLoggedIn = async () => {
  try {
    const response = await axios.get('/api/users/check', {
      withCredentials: true,
    });
    userLoggedIn.value = response.data.userLoggedIn;
    isAdmin.value = response.data.admin;
    return response.data.userLoggedIn;
  } catch (error) {
    console.error('Error checking user login status:', error);
    return false;
  }
};

const fetchUserDetails = async () => {
  try {
    const response = await axios.get('/api/users/get-details', {
      withCredentials: true,
    });
    userDetails.value = response.data;
  } catch (error) {
    console.error('Error fetching user details:', error);
  }
};

const fetchGames = async (page = 1) => {
  try {
    const offset = page - 1;
    const response = await axios.get('/api/users/get-games', {
      params: { offset },
      withCredentials: true,
    });
    games.value = response.data.items;
    const totalGames = response.data.totalItems;
    hasMoreGames.value =
      response.data.items.length === itemsPerPage &&
      currentPage.value * itemsPerPage < totalGames;
  } catch (error) {
    console.error('Error fetching games:', error);
  }
};

const goToDetails = (id) => {
  router.push({ name: 'details', params: { id } });
};

const nextPage = () => {
  if (hasMoreGames.value) {
    currentPage.value++;
    fetchGames(currentPage.value);
  }
};

const prevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
    fetchGames(currentPage.value);
  }
};

const getImageUrl = (imageId) => {
  return `/api/images/get/${imageId}`;
};

const goHome = () => {
  router.push({ name: 'home' });
};

const logout = async () => {
  try {
    await axios.post('/api/users/logout', {}, { withCredentials: true });
    userLoggedIn.value = false;
    await router.push({ name: 'home' });
  } catch (error) {
    console.error('Error logging out:', error);
  }
};

const deleteGame = async (gameId) => {
  try {
    await axios.delete(`/api/games/delete`, {
      params: { gameId },
      withCredentials: true,
    });
    await fetchGames(currentPage.value);
  } catch (error) {
    console.error('Error deleting game:', error);
  }
};

const editGame = async (gameId) => {
  try {
    const response = await axios.get(`/api/games/get/${gameId}`, {
      withCredentials: true,
    });
    editingGame.value = { ...response.data, imageFile: null };

    try {
      const imageResponse = await axios.get(`/api/images/get/${gameId}`, {
        responseType: 'blob',
      });
      const imageBlob = imageResponse.data;
      const imageFile = new File([imageBlob], 'current-image.jpg', { type: 'image/jpeg' });

      const dataTransfer = new DataTransfer();
      dataTransfer.items.add(imageFile);
      editingGame.value.imageFile = dataTransfer.files;

      editingGame.value.imagePreview = URL.createObjectURL(imageFile);
    } catch (imageError) {
      console.error('Error fetching game image:', imageError);
    }

    showEditGamePopup.value = true;
    setEditImageValue();
  } catch (error) {
    console.error('Error fetching game details:', error);
  }
};

const updateGame = async () => {
  try {
    const formData = new FormData();
    formData.append('name', editingGame.value.name);
    formData.append('price', parseFloat(editingGame.value.price));
    formData.append('description', editingGame.value.description);
    formData.append('releaseYear', editingGame.value.releaseYear);
    formData.append('region', editingGame.value.activationRegion.regionName);
    formData.append('platform', editingGame.value.activationPlatform.platformName);
    formData.append('publisher', editingGame.value.publisher.publisherName);
    formData.append('genre', editingGame.value.genre.name);
    formData.append('stock', editingGame.value.stock);
    formData.append('gameId', editingGame.value.id);

    if (editingGame.value.imageFile) {
      formData.append('image', editingGame.value.imageFile[0]);
    }

    await axios.post('/api/games/update-game', formData, {
      withCredentials: true,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    showEditGamePopup.value = false;

    window.location.reload();
  } catch (error) {
    console.error('Error updating the game:', error);
  }
};

const validatePrice = (event, formType) => {
  const input = event.target;
  const value = input.value;
  const regex = /^\d*(\.\d{0,2})?$/;

  if (!regex.test(value)) {
    input.value = value.substring(0, value.length - 1);
    if (formType === 'newGame') {
      newGame.price = input.value;
    } else {
      editingGame.price = input.value;
    }
  }
};

const setEditImageValue = () => {
  nextTick(() => {
    if (editImageInput.value && editingGame.value.imageFile) {
      editImageInput.value.files = editingGame.value.imageFile;
    }
  });
};

onMounted(async () => {
  const isLoggedIn = await checkUserLoggedIn();
  if (isLoggedIn) {
    await fetchUserDetails();
    await fetchGames(currentPage.value);
  } else {
    router.push({ name: 'login' });
  }
});
</script>

<style>
.navbar {
  display: flex;
  align-items: center;
  padding: 1.5rem;
  background-color: white;
  border-radius: 6px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  position: relative;
  margin-bottom: 2rem;
}

.navbar-title:hover {
  color: #4CAF50;
}

.game-title {
  font-size: 1.2rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

.navbar-title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  color: #333;
  font-size: 2.5rem;
  text-decoration: none;
  font-weight: bold;
}

.navbar-buttons {
  margin-left: auto;
  display: flex;
}

.navbar-buttons button {
  margin-left: 10px;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.navbar-left {
  display: flex;
}

.navbar-left button {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.navbar-buttons button:hover {
  background-color: #45a049;
}

.game-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  justify-content: flex-start;
  margin-left: 0;
}

.game-card {
  max-width: 240px;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: white;
  border-radius: 8px;
  padding: 1rem;
  transition: transform 0.2s,
  box-shadow 0.2s;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.game-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.game-card img {
  width: 100%;
  height: 300px;
  object-fit: cover;
  border-radius: 8px;
  margin-bottom: 1rem;
}

.game-card {
  font-weight: bold;
}

.game-actions {
  margin-top: auto;
  display: flex;
  justify-content: space-between;
}

.details-button,
.delete-button,
.edit-button {
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.details-button {
  background-color: #2196f3;
  color: white;
}

.details-button:hover {
  background-color: #1976d2;
}

.delete-button {
  background-color: #f44336;
  color: white;
}

.delete-button:hover {
  background-color: #d32f2f;
}

.edit-button {
  background-color: #4caf50;
  color: white;
}

.edit-button:hover {
  background-color: #45a049;
}

.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2rem;
}

textarea {
  resize: none;
  height: 100px;
  width: 100%;
  max-width: 100%;
  font-family: inherit;
  font-size: inherit;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 0.5rem;
  box-sizing: border-box;
}

.modal-content small {
  display: block;
  margin-bottom: 0.5rem;
  color: #666;
}

.pagination-controls button {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  margin: 0 1rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.pagination-controls button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.pagination-controls span {
  font-size: 1.25rem;
  color: #333;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 400px;
  max-width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.modal-content::-webkit-scrollbar {
  display: none;
}

.modal-content h2 {
  margin-bottom: 1rem;
}

.modal-content form {
  display: flex;
  flex-direction: column;
}

.modal-content label {
  margin-top: 0.5rem;
}

.modal-content input,
.modal-content textarea {
  margin-bottom: 0.5rem;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.modal-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 1rem;
}

.modal-actions button {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.modal-actions button[type='submit'] {
  background-color: #4caf50;
  color: white;
}

.modal-actions button[type='button'] {
  background-color: #f44336;
  color: white;
}

.add-game-button {
  margin-bottom: 1rem;
  padding: 0.5rem 1rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: block;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.no-scroll {
  overflow: hidden;
}

.no-listings-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 20px;
  margin-top: -1rem;
}

.no-listings-message {
  text-align: center;
  font-size: 1rem;
  color: #555;
}

.navbar-left {
  display: flex;
  gap: 10px;
}
</style>
