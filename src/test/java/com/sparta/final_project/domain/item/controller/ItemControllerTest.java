package com.sparta.final_project.domain.item.controller;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.item.dto.request.ItemCreateRequest;
import com.sparta.final_project.domain.item.dto.request.ItemUpdateRequest;
import com.sparta.final_project.domain.item.dto.response.ItemCreateResponse;
import com.sparta.final_project.domain.item.dto.response.ItemSimpleResponse;
import com.sparta.final_project.domain.item.dto.response.ItemUpdateResponse;
import com.sparta.final_project.domain.item.service.ItemService;
import com.sparta.final_project.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 테스트용 AuthUser 객체 초기화
        authUser = new AuthUser(1L, "test@example.com", "username", UserRole.USER); // AuthUser의 생성자는 필요에 따라 변경
    }

    @Test
    void createItem_ShouldReturnCreatedItem() {
        // Given
        ItemCreateRequest request = new ItemCreateRequest("Item Name", "Item Description", null);
        ItemCreateResponse response = new ItemCreateResponse(1L, "Item Name", "Item Description", null);
        when(itemService.createItem(any(ItemCreateRequest.class), any(AuthUser.class))).thenReturn(response);

        // When
        ResponseEntity<ItemCreateResponse> result = itemController.createItem(request, authUser);

        // Then
        verify(itemService, times(1)).createItem(any(ItemCreateRequest.class), any(AuthUser.class));
        assert(result.getStatusCode() == HttpStatus.CREATED);
        assert(result.getBody() != null);
        assert(result.getBody().getId().equals(1L));
    }

    @Test
    void getItem_ShouldReturnItem() {
        // Given
        Long itemId = 1L;
        ItemSimpleResponse response = new ItemSimpleResponse(itemId, "Item Name", "Item Description", null);
        when(itemService.getItem(itemId)).thenReturn(response);

        // When
        ResponseEntity<ItemSimpleResponse> result = itemController.getItem(itemId);

        // Then
        verify(itemService, times(1)).getItem(itemId);
        assert(result.getStatusCode() == HttpStatus.OK);
        assert(result.getBody() != null);
        assert(result.getBody().getId().equals(itemId));
    }

    @Test
    void updateItem_ShouldReturnUpdatedItem() {
        // Given
        Long itemId = 1L;
        ItemUpdateRequest request = new ItemUpdateRequest("Updated Name", "Updated Description", null);
        ItemUpdateResponse response = new ItemUpdateResponse(itemId, "Updated Name", "Updated Description", null, 1L);
        when(itemService.updateItem(eq(itemId), any(ItemUpdateRequest.class))).thenReturn(response);

        // When
        ResponseEntity<ItemUpdateResponse> result = itemController.updateItem(itemId, request);

        // Then
        verify(itemService, times(1)).updateItem(eq(itemId), any(ItemUpdateRequest.class));
        assert(result.getStatusCode() == HttpStatus.OK);
        assert(result.getBody() != null);
        assert(result.getBody().getId().equals(itemId));
    }

    @Test
    void deleteItem_ShouldReturnNoContent() {
        // Given
        Long itemId = 1L;
        doNothing().when(itemService).deleteItem(itemId);

        // When
        ResponseEntity<Void> result = itemController.deleteItem(itemId);

        // Then
        verify(itemService, times(1)).deleteItem(itemId);
        assert(result.getStatusCode() == HttpStatus.NO_CONTENT);
    }
}
