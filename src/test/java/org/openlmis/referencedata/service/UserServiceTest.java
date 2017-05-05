/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.referencedata.service;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openlmis.referencedata.domain.Facility;
import org.openlmis.referencedata.domain.Program;
import org.openlmis.referencedata.domain.Right;
import org.openlmis.referencedata.domain.RightType;
import org.openlmis.referencedata.domain.SupervisoryNode;
import org.openlmis.referencedata.domain.User;
import org.openlmis.referencedata.domain.UserBuilder;
import org.openlmis.referencedata.exception.ValidationMessageException;
import org.openlmis.referencedata.repository.FacilityRepository;
import org.openlmis.referencedata.repository.ProgramRepository;
import org.openlmis.referencedata.repository.RightRepository;
import org.openlmis.referencedata.repository.SupervisoryNodeRepository;
import org.openlmis.referencedata.repository.UserRepository;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@SuppressWarnings("PMD.TooManyMethods")
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(BlockJUnit4ClassRunner.class)
@PrepareForTest({UserService.class})
public class UserServiceTest {

  private static final String EXTRA_DATA_VALUE = "extraDataValue";
  private static final String FIRST_NAME_SEARCH = "FirstNameMatchesTwoUsers";
  private static final String EXTRA_DATA_KEY = "extraDataKey";
  private static final String EXTRA_DATA_PROP_NAME = "extraData";

  private static final UUID RIGHT_ID = UUID.randomUUID();
  private static final UUID WAREHOUSE_ID = UUID.randomUUID();
  private static final UUID SUPERVISORY_NODE_ID = UUID.randomUUID();
  private static final UUID PROGRAM_ID = UUID.randomUUID();

  @Mock
  private UserRepository userRepository;

  @Mock
  private SupervisoryNodeRepository supervisoryNodeRepository;

  @Mock
  private ProgramRepository programRepository;

  @Mock
  private FacilityRepository facilityRepository;

  @Mock
  private SupervisoryNode supervisoryNode;

  @Mock
  private RightRepository rightRepository;

  @Mock
  private Facility warehouse;

  @Mock
  private Program program;

  @Mock
  private Right right;

  @InjectMocks
  private UserService userService;

  private User user;
  private User user2;
  private User user3;
  private User user4;

  private Map<String, Object> userSearch;
  private Map<String, String> extraData;
  private ObjectMapper mapper = new ObjectMapper();
  private String extraDataString;
  private Map<String, Object> queryMap;

  @Before
  public void setUp() throws JsonProcessingException {
    user = generateUser();
    user2 = mock(User.class);
    user3 = mock(User.class);
    user4 = mock(User.class);
    userSearch = Collections.singletonMap("firstName", FIRST_NAME_SEARCH);
    extraData = Collections.singletonMap(EXTRA_DATA_KEY, EXTRA_DATA_VALUE);
    extraDataString = mapper.writeValueAsString(extraData);
    queryMap = new HashMap<>();
  }

  @Test
  @Ignore
  public void searchUsersShouldGetNoUsersIfSearchesGetDisjointedResults() {
    when(userRepository
        .searchUsers(
            any(String.class),
            eq(FIRST_NAME_SEARCH),
            any(String.class),
            any(String.class),
            any(Facility.class),
            any(Boolean.class),
            any(Boolean.class),
            any(Boolean.class)))
        .thenReturn(Arrays.asList(user, user2));

    when(userRepository.findByExtraData(extraDataString))
        .thenReturn(Arrays.asList(user3, user4));

    queryMap.putAll(userSearch);
    queryMap.put(EXTRA_DATA_PROP_NAME, extraData);

    List<User> receivedUsers = userService.searchUsers(queryMap);

    assertEquals(0, receivedUsers.size());
  }

  @Test
  @Ignore
  public void searchUsersShouldGetSomeUsersForOverlappingSearchResults() {
    when(userRepository
        .searchUsers(
            any(String.class),
            eq(FIRST_NAME_SEARCH),
            any(String.class),
            any(String.class),
            any(Facility.class),
            any(Boolean.class),
            any(Boolean.class),
            any(Boolean.class)))
        .thenReturn(Arrays.asList(user, user2));

    when(userRepository.findByExtraData(extraDataString))
        .thenReturn(Arrays.asList(user2, user3));

    queryMap.putAll(userSearch);
    queryMap.put(EXTRA_DATA_PROP_NAME, extraData);

    List<User> receivedUsers = userService.searchUsers(queryMap);

    assertEquals(1, receivedUsers.size());
    assertEquals(user2, receivedUsers.get(0));
  }

  @Test
  public void searchUsersShouldGetAllUsersIfSearchResultsAreTheSame() {
    when(userRepository
        .searchUsers(
            any(String.class),
            eq(FIRST_NAME_SEARCH),
            any(String.class),
            any(String.class),
            any(Facility.class),
            any(Boolean.class),
            any(Boolean.class),
            any(Boolean.class)))
        .thenReturn(Arrays.asList(user, user2));

    when(userRepository.findByExtraData(extraDataString))
        .thenReturn(Arrays.asList(user, user2));

    queryMap.putAll(userSearch);
    queryMap.put(EXTRA_DATA_PROP_NAME, extraData);

    List<User> receivedUsers = userService.searchUsers(queryMap);

    assertEquals(2, receivedUsers.size());
    assertTrue(receivedUsers.contains(user));
    assertTrue(receivedUsers.contains(user2));
  }

  @Test
  public void shouldSearchUsersWithExtraData() {
    when(userRepository.findByExtraData(extraDataString))
        .thenReturn(Arrays.asList(user, user2));

    queryMap.put(EXTRA_DATA_PROP_NAME, extraData);

    List<User> receivedUsers = userService.searchUsers(queryMap);

    assertEquals(2, receivedUsers.size());
    assertTrue(receivedUsers.contains(user));
    assertTrue(receivedUsers.contains(user2));
  }

  @Test
  public void searchUsersShouldNotSearchExtraDataIfParameterIsNullOrEmpty() {
    when(userRepository
        .searchUsers(
            any(String.class),
            eq(FIRST_NAME_SEARCH),
            any(String.class),
            any(String.class),
            any(Facility.class),
            any(Boolean.class),
            any(Boolean.class),
            any(Boolean.class)))
        .thenReturn(Arrays.asList(user, user2));

    queryMap.putAll(userSearch);

    List<User> receivedUsers = userService.searchUsers(queryMap);

    assertEquals(2, receivedUsers.size());
    assertTrue(receivedUsers.contains(user));
    assertTrue(receivedUsers.contains(user2));
    verify(userRepository, never()).findByExtraData(any(String.class));
  }

  @Test
  public void rightSearchShouldFindByDirectRightAssignments() {
    Set<User> expected = newHashSet(user, user2);
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(right);
    when(right.getType()).thenReturn(RightType.GENERAL_ADMIN);
    when(userRepository.findUsersByDirectRight(right))
        .thenReturn(expected);

    Set<User> users = userService.rightSearch(RIGHT_ID, null, null, null);

    assertEquals(expected, users);
    verify(userRepository).findUsersByDirectRight(right);
  }

  @Test
  public void rightSearchShouldFindByFulfillmentAssignment() {
    Set<User> expected = newHashSet(user, user2);
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(right);
    when(right.getType()).thenReturn(RightType.ORDER_FULFILLMENT);
    when(facilityRepository.findOne(WAREHOUSE_ID)).thenReturn(warehouse);
    when(warehouse.isWarehouse()).thenReturn(true);
    when(userRepository.findUsersByFulfillmentRight(right, warehouse))
        .thenReturn(expected);

    Set<User> users = userService.rightSearch(RIGHT_ID, null, null, WAREHOUSE_ID);

    assertEquals(expected, users);
    verify(userRepository).findUsersByFulfillmentRight(right, warehouse);
  }

  @Test
  public void rightSearchShouldFindBySupervisionAssignment() {
    Set<User> expected = newHashSet(user, user2);
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(right);
    when(right.getType()).thenReturn(RightType.SUPERVISION);
    when(supervisoryNodeRepository.findOne(SUPERVISORY_NODE_ID))
        .thenReturn(supervisoryNode);
    when(programRepository.findOne(PROGRAM_ID)).thenReturn(program);
    when(userRepository.findSupervisingUsersBy(right, supervisoryNode, program))
        .thenReturn(expected);

    Set<User> users = userService.rightSearch(RIGHT_ID, PROGRAM_ID,
        SUPERVISORY_NODE_ID, null);

    assertEquals(expected, users);
    verify(userRepository).findSupervisingUsersBy(right, supervisoryNode, program);
  }

  @Test(expected = ValidationMessageException.class)
  public void rightSearchShouldRequireExistingRight() {
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(null);

    try {
      userService.rightSearch(RIGHT_ID, PROGRAM_ID, SUPERVISORY_NODE_ID, null);
    } finally {
      verifyZeroInteractions(userRepository, facilityRepository,
          supervisoryNodeRepository, programRepository);
    }
  }

  @Test(expected = ValidationMessageException.class)
  public void rightSearchShouldRequireWarehouseIdForFulfillmentRights() {
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(right);
    when(right.getType()).thenReturn(RightType.ORDER_FULFILLMENT);

    try {
      userService.rightSearch(RIGHT_ID, null, null, null);
    } finally {
      verifyZeroInteractions(userRepository, supervisoryNodeRepository,
          facilityRepository, programRepository);
    }
  }

  @Test(expected = ValidationMessageException.class)
  public void rightSearchShouldThrowExceptionForNonExistentFacility() {
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(right);
    when(right.getType()).thenReturn(RightType.ORDER_FULFILLMENT);
    when(facilityRepository.findOne(WAREHOUSE_ID)).thenReturn(null);

    try {
      userService.rightSearch(RIGHT_ID, null, null, WAREHOUSE_ID);
    } finally {
      verify(facilityRepository).findOne(WAREHOUSE_ID);
      verifyZeroInteractions(supervisoryNodeRepository, programRepository,
          userRepository);
    }
  }

  @Test(expected = ValidationMessageException.class)
  public void rightSearchShouldThrowExceptionForNonWarehouseFacility() {
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(right);
    when(right.getType()).thenReturn(RightType.ORDER_FULFILLMENT);
    when(facilityRepository.findOne(WAREHOUSE_ID)).thenReturn(warehouse);
    when(warehouse.isWarehouse()).thenReturn(false);

    try {
      userService.rightSearch(RIGHT_ID, null, null, WAREHOUSE_ID);
    } finally {
      verify(warehouse).isWarehouse();
      verifyZeroInteractions(supervisoryNodeRepository, programRepository,
          userRepository);
    }
  }

  @Test(expected = ValidationMessageException.class)
  public void rightSearchShouldRequireProgramIdForSupervisoryRights() {
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(right);
    when(right.getType()).thenReturn(RightType.SUPERVISION);

    try {
      userService.rightSearch(RIGHT_ID, null, SUPERVISORY_NODE_ID, null);
    } finally {
      verifyZeroInteractions(userRepository, supervisoryNodeRepository,
          programRepository, facilityRepository);
    }
  }

  @Test(expected = ValidationMessageException.class)
  public void rightSearchShouldRequireSupervisoryNodeIdForSupervisoryRights() {
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(right);
    when(right.getType()).thenReturn(RightType.SUPERVISION);

    try {
      userService.rightSearch(RIGHT_ID, PROGRAM_ID, null, null);
    } finally {
      verifyZeroInteractions(userRepository, supervisoryNodeRepository,
          facilityRepository, programRepository);
    }
  }

  @Test(expected = ValidationMessageException.class)
  public void rightSearchShouldThrowExceptionForNonExistentProgram() {
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(right);
    when(right.getType()).thenReturn(RightType.SUPERVISION);
    when(supervisoryNodeRepository.findOne(SUPERVISORY_NODE_ID))
        .thenReturn(supervisoryNode);
    when(programRepository.findOne(PROGRAM_ID)).thenReturn(null);

    try {
      userService.rightSearch(RIGHT_ID, PROGRAM_ID, SUPERVISORY_NODE_ID, null);
    } finally {
      verify(programRepository).findOne(PROGRAM_ID);
      verifyZeroInteractions(facilityRepository, userRepository);
    }
  }

  @Test(expected = ValidationMessageException.class)
  public void rightSearchShouldThrowExceptionForNonExistentSupervisoryNode() {
    when(rightRepository.findOne(RIGHT_ID)).thenReturn(right);
    when(right.getType()).thenReturn(RightType.SUPERVISION);
    when(supervisoryNodeRepository.findOne(SUPERVISORY_NODE_ID))
        .thenReturn(null);
    when(programRepository.findOne(PROGRAM_ID)).thenReturn(program);

    try {
      userService.rightSearch(RIGHT_ID, PROGRAM_ID, SUPERVISORY_NODE_ID, null);
    } finally {
      verify(supervisoryNodeRepository).findOne(SUPERVISORY_NODE_ID);
      verifyZeroInteractions(facilityRepository, userRepository);
    }
  }

  private User generateUser() {
    return new UserBuilder("kota", "Ala", "ma", "test@mail.com")
        .setId(UUID.randomUUID())
        .setTimezone("UTC")
        .setHomeFacility(mock(Facility.class))
        .setVerified(false)
        .setActive(true)
        .setLoginRestricted(true)
        .setAllowNotify(true)
        .setExtraData(Collections.singletonMap(EXTRA_DATA_KEY, EXTRA_DATA_VALUE))
        .createUser();
  }
}
