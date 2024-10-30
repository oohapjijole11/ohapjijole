package com.sparta.final_project.domain.item;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.item.dto.request.ItemCreateRequest;
import com.sparta.final_project.domain.item.dto.response.ItemCreateResponse;
import com.sparta.final_project.domain.item.entity.Item;
import com.sparta.final_project.domain.item.repository.ItemRepository;
import com.sparta.final_project.domain.item.service.ItemService;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.entity.UserRole;
import com.sparta.final_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createItem_ValidRequest_Success() {
        // Arrange
        User user = new User("test@example.com", "password", "Test User", UserRole.USER);
        user.setId(1L);

        // 테스트에 사용할 이미지 URL 목록
        List<String> imageUrls = List.of("http://image.url");

        ItemCreateRequest request = new ItemCreateRequest("Test Item", "Item Description", imageUrls);
        AuthUser authUser = new AuthUser(user.getId(), user.getName(), user.getEmail(), user.getRole());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item item = invocation.getArgument(0);
            return new Item(item.getName(), item.getDescription(), item.getImageUrls(), item.getUser());
        });

        // Act
        ItemCreateResponse response = itemService.createItem(request, authUser);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getDescription()).isEqualTo(request.getDescription());
        assertThat(response.getImageUrls()).isEqualTo(request.getImageUrls());

        verify(userRepository, times(1)).findById(user.getId());
        verify(itemRepository, times(1)).save(any(Item.class));
    }
}