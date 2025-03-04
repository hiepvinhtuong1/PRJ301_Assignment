import axios from "axios"

export default class ApiService {
  static BASE_URL = "http://localhost:8081/hiep"

  // AUTH
  static async loginUser(user) {
    const response = await axios.post(`${this.BASE_URL}/auth/login`, user);
    console.log(response.data);
    return response
  }
}