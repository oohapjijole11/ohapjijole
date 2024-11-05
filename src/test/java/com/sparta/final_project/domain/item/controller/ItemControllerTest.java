package com.sparta.final_project.domain.item.controller;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.config.security.JwtAuthenticationToken;
import com.sparta.final_project.config.security.JwtUtil;
import com.sparta.final_project.config.security.SecurityConfig;
import com.sparta.final_project.domain.item.dto.request.ItemCreateRequest;
import com.sparta.final_project.domain.item.dto.request.ItemUpdateRequest;
import com.sparta.final_project.domain.item.dto.response.ItemCreateResponse;
import com.sparta.final_project.domain.item.dto.response.ItemSimpleResponse;
import com.sparta.final_project.domain.item.dto.response.ItemUpdateResponse;
import com.sparta.final_project.domain.item.service.ItemService;
import com.sparta.final_project.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class, JwtUtil.class})
class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;


    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "test@example.com", "username", UserRole.USER);
    }

    @Test
    void createItem_ShouldReturnCreatedItem() throws Exception {
        // Given
        ItemCreateRequest request = new ItemCreateRequest("Item Name", "Item Description", null);
        ItemCreateResponse response = new ItemCreateResponse(1L, "Item Name", "Item Description", null);
        when(itemService.createItem(any(ItemCreateRequest.class), any(AuthUser.class))).thenReturn(response);

        // When
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
        ResultActions result = mockMvc.perform(post("/items")
                .with(authentication(authenticationToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Item Name\",\"description\":\"Item Description\"}"));

        // Then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
        verify(itemService, times(1)).createItem(any(ItemCreateRequest.class), any(AuthUser.class));
    }

    @Test
    void getItem_ShouldReturnItem() throws Exception {
        // Given
        Long itemId = 1L;
        ItemSimpleResponse response = new ItemSimpleResponse(itemId, "Item Name", "Item Description", null);
        when(itemService.getItem(itemId)).thenReturn(response);

        // When
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
        ResultActions result = mockMvc.perform(get("/items/{id}", itemId)
                .with(authentication(authenticationToken)));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId));
        verify(itemService, times(1)).getItem(itemId);
    }

    @Test
    void updateItem_ShouldReturnUpdatedItem() throws Exception {
        // Given
        Long itemId = 1L;
        ItemUpdateRequest request = new ItemUpdateRequest("Updated Name", "Updated Description", null);
        ItemUpdateResponse response = new ItemUpdateResponse(itemId, "Updated Name", "Updated Description", null, 1L);
        when(itemService.updateItem(eq(itemId), any(ItemUpdateRequest.class))).thenReturn(response);

        // When
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
        ResultActions result = mockMvc.perform(put("/items/{id}", itemId)
                .with(authentication(authenticationToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Name\",\"description\":\"Updated Description\"}"));

        // Then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemId));
        verify(itemService, times(1)).updateItem(eq(itemId), any(ItemUpdateRequest.class));
    }

    @Test
    void deleteItem_ShouldReturnNoContent() throws Exception {
        // Given
        Long itemId = 1L;
        doNothing().when(itemService).deleteItem(itemId);

        // When
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
        ResultActions result = mockMvc.perform(delete("/items/{id}", itemId)
                .with(authentication(authenticationToken)));

        // Then
        result.andExpect(status().isNoContent());
        verify(itemService, times(1)).deleteItem(itemId);
    }
}
