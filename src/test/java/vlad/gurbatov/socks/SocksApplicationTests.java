package vlad.gurbatov.socks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SocksApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
	void contextLoads() {
	}

    @Test
    public void income() throws Exception {
        String dto = """
                {
                    "id": 0,
                    "color": "",
                    "cotton": 0,
                    "amount": 0
                }""";

        mockMvc.perform(post("/api/socks/income")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void outcome() throws Exception {
        String dto = """
                {
                    "id": 0,
                    "color": "",
                    "cotton": 0,
                    "amount": 0
                }""";

        mockMvc.perform(post("/api/socks/outcome")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void delete() throws Exception {
        String id = "0";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/socks")
                        .content(id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findById() throws Exception {
        mockMvc.perform(get("/api/socks/{0}", "0"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/api/socks"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void save() throws Exception {
        String dto = """
                {
                    "id": 0,
                    "color": "",
                    "cotton": 0,
                    "amount": 0
                }""";

        mockMvc.perform(post("/api/socks")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void update() throws Exception {
        String id = "0";
        String dto = """
                {
                    "id": 0,
                    "color": "",
                    "cotton": 0,
                    "amount": 0
                }""";

        mockMvc.perform(put("/api/socks")
                        .content(id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
