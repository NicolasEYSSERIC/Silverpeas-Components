/**
 * Copyright (C) 2000 - 2011 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://repository.silverpeas.com/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.mailinglist.service.model;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.silverpeas.mailinglist.service.model.beans.Activity;
import com.silverpeas.mailinglist.service.model.beans.MailingList;
import com.silverpeas.mailinglist.service.model.beans.MailingListActivity;
import com.silverpeas.mailinglist.service.model.beans.Message;
import com.silverpeas.mailinglist.service.model.dao.MessageDao;
import com.silverpeas.mailinglist.service.util.OrderBy;
import com.stratelia.webactiv.calendar.control.CalendarBm;
import com.stratelia.webactiv.calendar.control.CalendarRuntimeException;
import com.stratelia.webactiv.calendar.model.ToDoHeader;

public class MessageServiceImpl implements MessageService {
  private MessageDao messageDao;

  private int elementsPerPage = 10;

  private CalendarBm calendarBm;

  private static final int MSG_PER_ACTIVITY = 5;

  public int getElementsPerPage() {
    return elementsPerPage;
  }

  public void setElementsPerPage(final int elementsPerPage) {
    this.elementsPerPage = elementsPerPage;
  }

  public MessageDao getMessageDao() {
    return messageDao;
  }

  public void setMessageDao(final MessageDao messageDao) {
    this.messageDao = messageDao;
  }

  /*
   * (non-Javadoc)
   * @see com.silverpeas.mailinglist.service.model.MessageService#saveMessage(com
   * .silverpeas.mailinglist.service.model.beans.Message)
   */
  public String saveMessage(final Message message) {
    if (message == null) {
      return null;
    }
    String id = message.getId();
    if (id == null) {
      id = messageDao.saveMessage(message);
      MessageIndexer.indexMessage(message);
    } else {
      messageDao.updateMessage(message);
    }
    return id;
  }

  /*
   * (non-Javadoc)
   * @see com.silverpeas.mailinglist.service.model.MessageService#getMessage(java .lang.String)
   */
  public Message getMessage(final String id) {
    if (id == null) {
      return null;
    }
    return messageDao.findMessageById(id);
  }

  /*
   * (non-Javadoc)
   * @see com.silverpeas.mailinglist.service.model.MessageService#listMessages(com
   * .silverpeas.mailinglist.service.model.beans.MailingList, int, int)
   */
  public List<Message> listMessages(final MailingList mailingList,
      final int pageNumber, final OrderBy orderBy) {
    return messageDao
        .listAllMessagesOfMailingList(mailingList.getComponentId(), pageNumber,
        this.elementsPerPage, orderBy);
  }

  /*
   * (non-Javadoc)
   * @see com.silverpeas.mailinglist.service.model.MessageService#listDisplayableMessages
   * (com.silverpeas.mailinglist.service.model.beans.MailingList, int, int)
   */
  public List<Message> listDisplayableMessages(final MailingList mailingList,
      final int month, final int year, final int pageNumber,
      final OrderBy orderBy) {
    return messageDao.listDisplayableMessagesOfMailingList(mailingList
        .getComponentId(), month, year, pageNumber, this.elementsPerPage,
        orderBy);
  }

  /*
   * (non-Javadoc)
   * @seecom.silverpeas.mailinglist.service.model.MessageService# listUnmoderatedeMessages
   * (com.silverpeas.mailinglist.service.model.beans.MailingList, int, int)
   */
  public List<Message> listUnmoderatedeMessages(final MailingList mailingList,
      final int pageNumber, final OrderBy orderBy) {
    return messageDao.listUnmoderatedMessagesOfMailingList(mailingList
        .getComponentId(), pageNumber, this.elementsPerPage, orderBy);
  }

  /*
   * (non-Javadoc)
   * @seecom.silverpeas.mailinglist.service.model.MessageService#
   * getNumberOfPagesForUnmoderatedMessages
   * (com.silverpeas.mailinglist.service.model.beans.MailingList, int)
   */
  public int getNumberOfPagesForUnmoderatedMessages(
      final MailingList mailingList) {
    final int nbElements = messageDao
        .listTotalNumberOfUnmoderatedMessages(mailingList.getComponentId());
    return getNumberOfPages(nbElements);
  }

  /*
   * (non-Javadoc)
   * @seecom.silverpeas.mailinglist.service.model.MessageService#
   * getNumberOfPagesForDisplayableMessages
   * (com.silverpeas.mailinglist.service.model.beans.MailingList, int)
   */
  public int getNumberOfPagesForDisplayableMessages(
      final MailingList mailingList) {
    final int nbElements = messageDao
        .listTotalNumberOfDisplayableMessages(mailingList.getComponentId());
    return getNumberOfPages(nbElements);
  }

  /*
   * (non-Javadoc)
   * @seecom.silverpeas.mailinglist.service.model.MessageService# getNumberOfPagesForAllMessages
   * (com.silverpeas.mailinglist.service.model.beans.MailingList, int)
   */
  public int getNumberOfPagesForAllMessages(final MailingList mailingList) {
    final int nbElements = messageDao.listTotalNumberOfMessages(mailingList
        .getComponentId());
    return getNumberOfPages(nbElements);
  }

  private int getNumberOfPages(final int nbElements) {
    int nbPages = nbElements / this.elementsPerPage;
    if (nbElements % this.elementsPerPage != 0) {
      nbPages = nbPages + 1;
    }
    return nbPages;
  }

  public MailingListActivity getActivity(final MailingList mailingList) {
    if (mailingList == null) {
      return null;
    }
    List<Message> messages = messageDao.listActivityMessages(mailingList
        .getComponentId(), MSG_PER_ACTIVITY, new OrderBy("sentDate", false));
    List<Activity> activities = messageDao.listActivity(mailingList
        .getComponentId());
    Collections.sort(activities);
    return new MailingListActivity(messages, activities);
  }

  @SuppressWarnings("unchecked")
  public void deleteMessage(String id) {
    Message message = messageDao.findMessageById(id);
    if (message != null) {
      messageDao.deleteMessage(message);
      MessageIndexer.unindexMessage(message);
      try {
        Collection<ToDoHeader> todos = calendarBm.getOrganizerToDos(message
            .getComponentId());
        if (todos != null && !todos.isEmpty()) {
          for (ToDoHeader todo : todos) {
            if (id.equalsIgnoreCase(todo.getDescription())) {
              todo.setCompletedDate(new Date());
              todo.setPercentCompleted(100);
              getCalendarBm().updateToDo(todo);
              return;
            }
          }
        }
      } catch (RemoteException e) {
        e.printStackTrace();
      } catch (CalendarRuntimeException e) {
        e.printStackTrace();
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void moderateMessage(String id) {
    Message message = messageDao.findMessageById(id);
    if (message != null) {
      message.setModerated(true);
      messageDao.updateMessage(message);
      MessageIndexer.indexMessage(message);
    }
    try {
      Collection<ToDoHeader> todos = calendarBm.getOrganizerToDos(message
          .getComponentId());
      if (todos != null && !todos.isEmpty()) {
        for (ToDoHeader todo : todos) {
          if (id.equalsIgnoreCase(todo.getDescription())) {
            todo.setCompletedDate(new Date());
            todo.setPercentCompleted(100);
            getCalendarBm().updateToDo(todo);
            return;
          }
        }
      }
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (CalendarRuntimeException e) {
      e.printStackTrace();
    }
  }

  public List<Message> listDisplayableMessages(MailingList mailingList,
      int number, OrderBy orderBy) {
    return messageDao.listDisplayableMessagesOfMailingList(mailingList
        .getComponentId(), -1, -1, 0, number, orderBy);
  }

  public CalendarBm getCalendarBm() {
    return calendarBm;
  }

  public void setCalendarBm(CalendarBm calendarBm) {
    this.calendarBm = calendarBm;
  }

  @Override
  public int getTotalNumberOfMessages(MailingList mailingList) {
    return messageDao.listTotalNumberOfMessages(mailingList.getComponentId());
  }

}
