package vlad.gurbatov.socks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vlad.gurbatov.socks.controller.SockController;
import vlad.gurbatov.socks.entity.Sock;
import vlad.gurbatov.socks.entity.dto.mapper.SockMapper;
import vlad.gurbatov.socks.service.SockService;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SocksApplicationTests {

    @Mock
    private SockService sockService;
    @Mock
    private SockMapper sockMapper;
    @InjectMocks
    private SockController sockController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sockController).build();
    }

    private List<Sock> getSocks() {
        Sock firstSock = new Sock();
        Sock secondSock = new Sock();
        firstSock.setId(1L);
        firstSock.setColor("Black");
        firstSock.setAmount(10);
        firstSock.setCottonPercentage(15);
        secondSock.setId(2L);
        secondSock.setColor("White");
        secondSock.setAmount(15);
        secondSock.setCottonPercentage(50);
        return List.of(firstSock, secondSock);
    }

    @Test
    public void deleteSuccess() throws Exception {
        String id = "1";
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/socks/{0}", id)
                        .content(id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void findByIdSuccess() throws Exception {
        when(sockService.findById(1L)).thenReturn(Optional.ofNullable(getSocks().get(0)));
        mockMvc.perform(get("/api/socks/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void findByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/socks/1000"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    //add filters and look if it works
    @Test
    public void getAll() throws Exception {
        when(sockService.getAll(null, null, null, null, null)).thenReturn(getSocks());
        mockMvc.perform(get("/api/socks"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void saveSuccess() throws Exception {
        String dto = """
                {
                    "color": "black",
                    "cottonPercentage": 50,
                    "amount": 10
                }""";

        mockMvc.perform(post("/api/socks")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void saveFailInvalidArguments() throws Exception {
        String dto = """
                {
                    "color": "",
                    "cottonPercentage": 510,
                    "amount": -25
                }""";

        mockMvc.perform(post("/api/socks")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void updateSuccess() throws Exception {
        Long id = 1L;
        String dto = """
                {
                    "color": "black",
                    "cottonPercentage": 20,
                    "amount": 15
                }""";

        mockMvc.perform(put("/api/socks/{id}", id)
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void updateFailInvalidArguments() throws Exception {
        String dto = """
                {
                    "color": "",
                    "cottonPercentage": 510,
                    "amount": -25
                }""";

        mockMvc.perform(put("/api/socks/{0}", 1L)
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
