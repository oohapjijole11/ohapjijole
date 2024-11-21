package com.sparta.final_project.domain.item.service;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.item.dto.request.ItemCreateRequest;
import com.sparta.final_project.domain.item.dto.request.ItemUpdateRequest;
import com.sparta.final_project.domain.item.dto.response.ItemCreateResponse;
import com.sparta.final_project.domain.item.dto.response.ItemSimpleResponse;
import com.sparta.final_project.domain.item.dto.response.ItemUpdateResponse;
import com.sparta.final_project.domain.item.entity.Item;
import com.sparta.final_project.domain.item.repository.ItemRepository;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.entity.UserRole;
import com.sparta.final_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ItemServiceTest {
    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CacheManager cacheManager; // 캐시 매니저를 Mock합니다.

    private Cache cache; // 캐시를 Mock합니다.

    private Item item; // Item 객체를 클래스 레벨에서 정의합니다.

    private User user; // User 객체를 클래스 레벨에서 정의합니다.

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cache = mock(Cache.class); // Cache를 Mock합니다.
        when(cacheManager.getCache("items")).thenReturn(cache); // "items" 캐시를 반환하도록 설정합니다.
    }

    @Test
    public void createItem_ValidRequest_Success() {
        // Given: User 및 요청 데이터 생성
        User user = new User("test@example.com", "password", "Test User", UserRole.USER, "http://slack-url.example.com");
        ItemCreateRequest request = new ItemCreateRequest("Test Item", "This is a test item.", List.of("imageUrl1", "imageUrl2"));


        // Mock UserRepository
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Mock ItemRepository
        Item savedItem = new Item(request.getName(), request.getDescription(), request.getImageUrls(), user);
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        // When: 상품 생성 메서드 호출
        ItemCreateResponse response = itemService.createItem(request, new AuthUser(user.getId(),user.getName(),user.getEmail(),user.getRole()));

        // Then: 결과 검증
        assertNotNull(response);
        assertEquals(savedItem.getId(), response.getId());
        assertEquals(savedItem.getName(), response.getName());
        assertEquals(savedItem.getDescription(), response.getDescription());
        assertEquals(savedItem.getImageUrls(), response.getImageUrls());
    }

    @Test
    public void getItem_ValidId_Success() {
        // Given: Item 및 ID 생성
        Long itemId = 1L; // 예상 ID 설정
        User user = new User("test@naver.com", "testpassword", "Test User", UserRole.USER, "http://slack-url.example.com");

        // Mock Item 객체 생성
        Item item = Mockito.mock(Item.class);
        when(item.getId()).thenReturn(itemId); // item의 getId()가 itemId를 반환하도록 설정
        when(item.getName()).thenReturn("Test Item");
        when(item.getDescription()).thenReturn("This is a test item.");
        when(item.getImageUrls()).thenReturn(List.of("imageUrl1", "imageUrl2"));
        when(item.getUser()).thenReturn(user);

        // Mock ItemRepository
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // When: 상품 조회 메서드 호출
        ItemSimpleResponse response = itemService.getItem(itemId);

        // Then: 결과 검증
        assertNotNull(response);
        assertEquals(itemId, response.getId());
        assertEquals("Test Item", response.getName());
        assertEquals("This is a test item.", response.getDescription());
        assertEquals(List.of("imageUrl1", "imageUrl2"), response.getImageUrls());
    }

    @Test
    public void getItem_InvalidId_ThrowsException() {
        // Given: 존재하지 않는 ID 설정
        Long invalidId = 999L;

        // Mock ItemRepository
        when(itemRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then: 예외가 발생해야 함
        assertThrows(OhapjijoleException.class, () -> {
            itemService.getItem(invalidId);
        });
    }


    @Test
    public void updateItem_ValidId_Success() {
        // Given
        Long itemId = 1L; // 수정할 아이템의 ID
        ItemUpdateRequest request = new ItemUpdateRequest("Updated Item", "Updated description", List.of("updatedImageUrl"));
        User mockUser = new User("testuser@example.com", "password", "Test User", UserRole.USER, "http://slack-url.example.com");

        // Create an Item instance with an ID and existing image URL
        Item item = new Item("Original Item", "Original description", List.of("originalImageUrl"), mockUser);
        ReflectionTestUtils.setField(item, "id", itemId); // Item의 ID 필드 설정

        // Mocking behavior for itemRepository
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item)); // Mock: 아이템이 존재하는 경우
        when(itemRepository.save(any(Item.class))).thenReturn(item); // Mock: 아이템 저장에 대한 설정

        // When
        ItemUpdateResponse response = itemService.updateItem(itemId, request);

        // Then
        assertNotNull(response);
        assertEquals(itemId, response.getId());
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(request.getImageUrls(), response.getImageUrls());

        // Verify interactions
        verify(itemRepository).findById(itemId); // findById 메소드 호출 확인
        verify(itemRepository).save(any(Item.class)); // save 메소드 호출 확인
    }
    // 상품 삭제 테스트
    @Test
    public void deleteItem_ValidId_Success() {
        // Given: Item ID 설정
        Long itemId = 1L;

        // Mock ItemRepository
        when(itemRepository.existsById(itemId)).thenReturn(true);

        // When: 상품 삭제 메서드 호출
        itemService.deleteItem(itemId);

        // Then: 결과 검증
        verify(itemRepository).deleteById(itemId); // deleteById가 호출되는지 확인
    }

    @Test
    public void deleteItem_InvalidId_ThrowsException() {
        // Given: 존재하지 않는 ID 설정
        Long invalidId = 999L;

        // Mock ItemRepository
        when(itemRepository.existsById(invalidId)).thenReturn(false);

        // When & Then: 예외가 발생해야 함
        assertThrows(OhapjijoleException.class, () -> {
            itemService.deleteItem(invalidId);
        });
    }
}