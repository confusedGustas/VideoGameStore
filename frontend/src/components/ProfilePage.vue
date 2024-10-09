<template>
  <div>
    <nav class="navbar">
      <div class="navbar-buttons">
        <button @click="goHome">Home</button>
        <button v-if="userLoggedIn" @click="logout" class="logout">Logout</button>
      </div>
      <h1 class="navbar-title">Profile</h1>
    </nav>

    <div v-if="userDetails">
      <h1>Hello {{ userDetails.username }}</h1>

      <div v-if="games.length > 0">
        <p>Your Listings:</p>
        <div class="game-grid">
          <div v-for="game in games" :key="game.id" class="game-card" @click="goToDetails(game.id)">
            <img :src="getImageUrl(game.image)" :alt="game.name" />
            <h3>{{ game.name }}</h3>
            <p class="price">${{ game.price.toFixed(2) }}</p>
            <p class="genre">{{ game.genre.name }}</p>
            <p class="publisher">{{ game.publisher.publisherName }}</p>
            <p class="platform">{{ game.activationPlatform.platformName }}</p>
          </div>
        </div>

        <div class="pagination-controls">
          <button @click="prevPage" :disabled="currentPage === 1">Previous</button>
          <span>Page {{ currentPage }}</span>
          <button @click="nextPage" :disabled="!hasMoreGames">Next</button>
        </div>
      </div>
      <div v-else>
        <p>No listings available.</p>
      </div>
    </div>

    <div v-else>
      <p>Redirecting to login...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from "vue-router";

const router = useRouter();
const userDetails = ref(null);
const games = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;
const hasMoreGames = ref(true);
const userLoggedIn = ref(false);

const checkUserLoggedIn = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/users/check', {
      withCredentials: true
    });
    userLoggedIn.value = response.data.userLoggedIn;
    return response.data.userLoggedIn;
  } catch (error) {
    console.error('Error checking user login status:', error);
    return false;
  }
};

const fetchUserDetails = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/users/get-details', {
      withCredentials: true
    });
    userDetails.value = response.data;
  } catch (error) {
    console.error('Error fetching user details:', error);
  }
};

const fetchGames = async (page = 1) => {
  try {
    const offset = (page - 1);

    const response = await axios.get('http://localhost:8080/api/users/get-games', {
      params: { offset },
      withCredentials: true
    });

    games.value = response.data.items;
    const totalGames = response.data.totalItems;
    hasMoreGames.value = response.data.items.length === itemsPerPage && (currentPage.value * itemsPerPage) < totalGames;
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

const getImageUrl = (imageName) => {
  return `http://localhost:8080/api/images/get/${imageName}`;
};

const goHome = () => {
  router.push({ name: 'home' });
};

const logout = async () => {
  try {
    await axios.post('http://localhost:8080/api/users/logout', {}, { withCredentials: true });
    userLoggedIn.value = false;
    await router.push({name: 'home'});
  } catch (error) {
    console.error('Error logging out:', error);
  }
};

onMounted(async () => {
  const loggedIn = await checkUserLoggedIn();

  if (!loggedIn) {
    await router.push({ name: 'login' });
  } else {
    await fetchUserDetails();
    await fetchGames(currentPage.value);
  }
});
</script>

<style scoped>
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

.navbar-title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  color: #333;
  font-size: 2.5rem;
}

.navbar-buttons {
  margin-left: auto;
}

.navbar-buttons button {
  margin-left: 10px;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  background-color: #4CAF50;
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
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.game-card {
  background-color: white;
  border-radius: 8px;
  padding: 1rem;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
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

.game-card .price {
  font-weight: bold;
  color: #4CAF50;
}

.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2rem;
}

.pagination-controls button {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  margin: 0 1rem;
  background-color: #4CAF50;
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
</style>
