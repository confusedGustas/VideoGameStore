<template>
  <div>
    <nav class="navbar">
      <div class="navbar-left"></div>
      <router-link to="/admin" class="navbar-title" exact-active-class="active-link">Admin Panel</router-link>
      <div class="navbar-buttons">
        <button @click="goHome">Home</button>
        <button @click="logout" class="logout">Logout</button>
      </div>
    </nav>
    <h1>Admin Panel</h1>

    <div class="controls">
      <input type="text" v-model="search" placeholder="Search users" />
      <button @click="fetchUsers">Search</button>
    </div>

    <div class="table-container">
      <table class="user-table">
        <thead>
        <tr>
          <th>Username</th>
          <th>Email</th>
          <th>Role</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.role }}</td>
          <td>{{ user.enabled ? 'Enabled' : 'Disabled' }}</td>
          <td>
            <button :class="user.enabled ? 'btn-disable' : 'btn-enable'" @click="toggleUserStatus(user)">
              {{ user.enabled ? 'Disable' : 'Enable' }}
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination-controls">
      <button @click="prevPage" :disabled="offset === 0">Previous</button>
      <span>Page {{ currentPage }}</span>
      <button @click="nextPage" :disabled="!hasMoreUsers">Next</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const users = ref([]);
const offset = ref(0);
const limit = ref(20);
const search = ref('');
const currentPage = ref(1);
const hasMoreUsers = ref(true);
const userLoggedIn = ref(false);
const isAdmin = ref(false);

const fetchUsers = async () => {
  try {
    const response = await axios.get('/api/admin/get-users', {
      params: {
        limit: limit.value,
        offset: offset.value,
        search: search.value,
      },
    });

    users.value = response.data.users;
    hasMoreUsers.value = users.value.length === limit.value;
  } catch (error) {
    console.error('Error fetching users:', error);
  }
};

const nextPage = () => {
  offset.value += 1;
  currentPage.value++;
  fetchUsers();
};

const prevPage = () => {
  if (offset.value >= 1) {
    offset.value -= 1
    currentPage.value--;
    fetchUsers();
  }
};

const toggleUserStatus = async (user) => {
  try {
    const userId = user.id;
    const state = !user.enabled;
    const url = `/api/admin/change-user-status?userId=${userId}&state=${state}`;
    await axios.post(url);
    user.enabled = state;
  } catch (error) {
    console.error('Error toggling user status:', error);
  }
};

const goHome = () => {
  router.push({ name: 'home' });
};

const logout = async () => {
  try {
    await axios.post('/api/users/logout', {}, { withCredentials: true });
    router.push({ name: 'home' });
  } catch (error) {
    console.error('Error logging out:', error);
  }
};

const checkUserLoggedIn = async () => {
  try {
    const response = await axios.get('/api/users/check', { withCredentials: true });
    userLoggedIn.value = response.data.userLoggedIn;
    isAdmin.value = response.data.admin;

    if (!response.data.userLoggedIn) {
      await router.push({name: 'login'});
    }

    return response.data.userLoggedIn;
  } catch (error) {
    console.error('Error checking user login status:', error);
    await router.push({name: 'login'});
    return false;
  }
};

onMounted(async () => {
  const loggedIn = await checkUserLoggedIn();
  if (loggedIn) {
    await fetchUsers();
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

.navbar-buttons button:hover {
  background-color: #45a049;
}

.controls {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.controls input {
  padding: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 1rem;
}

.controls button {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.controls button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.controls button:not(:disabled) {
  background-color: #4caf50;
  color: white;
}

.controls button:not(:disabled):hover {
  background-color: #45a049;
}

.table-container {
  margin: 2rem 0;
  overflow-x: auto;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

th, td {
  padding: 0.75rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f4f4f4;
}

tr:hover {
  background-color: #f1f1f1;
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

button {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-enable {
  background-color: #4caf50;
}

.btn-enable:hover {
  background-color: #45a049;
}

.btn-disable {
  background-color: #f44336;
}

.btn-disable:hover {
  background-color: #d32f2f;
}
</style>
