/*
 * Copyright (C) 2000 - 2011 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection withWriter Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have recieved a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.silverpeas.questionReply.web;

import com.silverpeas.web.TestResources;
import javax.inject.Inject;
import javax.inject.Named;
import static org.junit.Assert.assertNotNull;

/**
 * A wrapper of resources used in the tests of the Question and Reply web services.
 */
@Named(TestResources.TEST_RESOURCES_NAME)
public class QuestionReplyTestResources extends TestResources {

  public static final String COMPONENT_INSTANCE_ID = "questionReply12";
  public static final String QUESTION_RESOURCE_PATH = "questionreply/" + COMPONENT_INSTANCE_ID
          + "/questions";
  public static final String REPLY_RESOURCE_PATH = "questionreply/" + COMPONENT_INSTANCE_ID
          + "/replies";
  @Inject
  private MockableQuestionManager questionManager;

  public MockableQuestionManager getMockableQuestionManager() {
    assertNotNull(questionManager);
    return questionManager;
  }
}
