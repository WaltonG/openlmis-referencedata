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

package org.openlmis.referencedata.repository;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.openlmis.referencedata.domain.Lot;
import org.openlmis.referencedata.domain.TradeItem;
import org.openlmis.referencedata.testbuilder.LotDataBuilder;
import org.openlmis.referencedata.testbuilder.TradeItemDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SuppressWarnings("PMD.TooManyMethods")
public class LotRepositoryIntegrationTest extends BaseCrudRepositoryIntegrationTest<Lot> {

  @Autowired
  private LotRepository repository;

  @Autowired
  private TradeItemRepository tradeItemRepository;

  @Autowired
  private LotRepository lotRepository;

  @Override
  LotRepository getRepository() {
    return this.repository;
  }

  private LocalDate now = LocalDate.now();

  private Lot lotOne;
  private Lot lotTwo;

  @Override
  Lot generateInstance() {
    TradeItem tradeItem = new TradeItemDataBuilder().buildAsNew();
    tradeItemRepository.save(tradeItem);

    return new LotDataBuilder()
        .withTradeItem(tradeItem)
        .buildAsNew();
  }

  @Before
  public void setUp() {
    lotOne = generateInstance();
    lotTwo = generateInstance();

    lotRepository.save(generateInstance());
    lotRepository.save(generateInstance());
    lotRepository.save(generateInstance());
    lotRepository.save(lotOne);
    lotRepository.save(lotTwo);
  }

  @Test
  public void shouldFindLotsWithSimilarCode() {
    Lot expected = lotRepository.save(generateInstance());

    Page<Lot> lotPage = lotRepository.search(
        null,
        null,
        expected.getLotCode(),
        null,
        null
    );

    assertEquals(1, lotPage.getNumberOfElements());
    assertEquals(expected, lotPage.getContent().get(0));
  }

  @Test
  public void shouldEnableAddingLotsWithSimilarCodeForDifferentTradeItems() {
    Lot lotOne = lotRepository.save(generateInstance());
    Lot lotTwo = generateInstance();
    lotTwo.setLotCode(lotOne.getLotCode());
    lotRepository.save(lotTwo);

    Page<Lot> lotPage = lotRepository.search(
        null,
        null,
        lotOne.getLotCode(),
        null,
        null
    );

    assertEquals(2, lotPage.getNumberOfElements());
    assertEquals(lotOne, lotPage.getContent().get(0));
    assertEquals(lotTwo, lotPage.getContent().get(1));
  }

  @Test
  public void shouldFindLotsByExpirationDate() {
    Lot expected = generateInstance();
    expected.setExpirationDate(now);
    expected = lotRepository.save(expected);

    Page<Lot> lotPage = lotRepository.search(null, now, null, null, null);

    assertEquals(1, lotPage.getNumberOfElements());
    assertEquals(expected, lotPage.getContent().get(0));
  }

  @Test
  public void shouldFindLotsByTradeItem() {
    Lot expected = lotRepository.save(generateInstance());

    Page<Lot> lotPage = lotRepository.search(
        Collections.singletonList(expected.getTradeItem()),
        null,
        null,
        null,
        null
    );

    assertEquals(1, lotPage.getNumberOfElements());
    assertEquals(expected, lotPage.getContent().get(0));
  }

  @Test
  public void shouldFindLotsByMultipleTradeItems() {
    Lot expected = lotRepository.save(generateInstance());
    Lot expected2 = lotRepository.save(generateInstance());

    Page<Lot> lotPage = lotRepository.search(
        ImmutableList.of(expected.getTradeItem(), expected2.getTradeItem()),
        null,
        null,
        null,
        null
    );

    assertEquals(2, lotPage.getNumberOfElements());
    assertEquals(expected, lotPage.getContent().get(0));
    assertEquals(expected2, lotPage.getContent().get(1));
  }

  @Test
  public void shouldFindAllLotsIfSearchByEmptyTradeItemList() {
    Page<Lot> lotPage = lotRepository.search(
        Collections.emptyList(),
        null,
        null,
        null,
        null
    );

    assertEquals(5, lotPage.getNumberOfElements());
  }

  @Test
  public void shouldFindLotsByAllParameters() {
    Lot expected = lotRepository.save(generateInstance());

    Page<Lot> lotPage = lotRepository.search(
        Collections.singletonList(expected.getTradeItem()),
        expected.getExpirationDate(),
        expected.getLotCode(),
        Collections.singletonList(expected.getId()),
        null
    );

    assertEquals(1, lotPage.getNumberOfElements());
    assertEquals(expected, lotPage.getContent().get(0));
  }

  @Test
  public void shouldFindLotsByIds() {
    Lot instanceOne = generateInstance();
    repository.save(instanceOne);

    Lot instanceTwo = generateInstance();
    repository.save(instanceTwo);

    Page<Lot> lotPage = lotRepository.search(
        null,
        null,
        null,
        Arrays.asList(instanceOne.getId(), instanceTwo.getId()),
        null
    );

    assertEquals(2, lotPage.getNumberOfElements());
    assertEquals(lotPage.getContent(), Arrays.asList(instanceOne, instanceTwo));
  }

  @Test
  public void shouldReturnAllIfNoParamIsGiven() {
    Page<Lot> lotPage = lotRepository.search(null, null, null, null, null);

    assertEquals(5, lotPage.getNumberOfElements());
  }

  @Test
  public void shouldReturnTrueIfLotWithGivenCodeAndTradeItemIdExists() {
    Lot expected = lotRepository.save(generateInstance());

    boolean exists = lotRepository.existsByLotCodeAndTradeItemId(expected.getLotCode(),
        expected.getTradeItem().getId());

    assertEquals(true, exists);
  }

  @Test
  public void shouldReturnCorrectDate() {
    LocalDate date = LocalDate.now();

    Lot entity = generateInstance();
    entity.setExpirationDate(date);
    lotRepository.save(entity);

    Page<Lot> lotPage = lotRepository.search(
        null,
        null,
        entity.getLotCode(),
        null,
        null
    );

    assertEquals(1, lotPage.getNumberOfElements());
    assertEquals(date, lotPage.getContent().get(0).getExpirationDate());
  }

  @Test
  public void shouldRespectPaginationParameters() {
    Pageable pageable = new PageRequest(1, 3);

    Page<Lot> lotPage = lotRepository.search(
        null,
        null,
        null,
        null,
        pageable
    );

    assertEquals(2, lotPage.getNumberOfElements());
    assertEquals(lotOne, lotPage.getContent().get(0));
    assertEquals(lotTwo, lotPage.getContent().get(1));
  }
}
