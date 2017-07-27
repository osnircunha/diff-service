package com.ocunha.odiff.controller;

import com.ocunha.odiff.ServiceExceptionMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link DiffController}
 *
 * Created by osnircunha on 26/07/17.
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void postFile_should_returnResponseOk_when_postValidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    public void postFile_should_returnResponseBadRequest_when_postEmptyData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(""))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()));

    }

    @Test
    public void postFile_should_returnResponseInternalServerError_when_postInvalidData() throws Exception {
        expectedException.expectCause(ServiceExceptionMatcher.assertMessage("Invalid parameters"));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(" "))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    }

    @Test
    public void postFile_should_returnResponseInternalServerError_when_postInvalidIdUrl() throws Exception {
        expectedException.expectCause(ServiceExceptionMatcher.assertMessage("Invalid parameters"));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/ /left")
                .content(getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    }

    @Test
    public void postFile_should_returnResponseInternalServerError_when_postInvalidTypeUrl() throws Exception {
        expectedException.expectCause(ServiceExceptionMatcher.assertMessage("Invalid parameters"));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/invalid")
                .content(getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    }

    @Test
    public void getDiff_should_returnResponseOkAndResultAreEqualsMessage_when_equalFilesWereSubmited() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/right")
                .content(getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/123"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getDiff_should_returnResponseOkAndResultHasDifferentByteLenghtMessage_when_equalFilesAreSubmited() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/right")
                .content(getJsonEncoded23Lines1316BytesEmailUpperCase()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Different size. Left is 2064 bytes and Right is 1316 bytes"));
    }

    @Test
    public void getDiff_should_returnResponseOkAndResultHasDifferentLineNumberMessage_when_filesWithDifferentLineNumberAreSubmited() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(getJsonEncoded36Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/right")
                .content(getJsonEncoded37Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("Different line numbers. Left has 36 lines and Right has 37 lines"));
    }

    @Test
    public void getDiff_should_returnResponseOkAndResultHasDifferentContentMessage_when_filesWithDifferentLineContentAreSubmited() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/left")
                .content(getJsonEncoded37Lines2064Bytes()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .put("/v1/diff/123/right")
                .content(getJsonEncoded37Lines2064BytesEmailUpperCase()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("5 difference(s) found. Line(s): 6, 13, 20, 27, 34"));
    }

    @Test
    public void getDiff_should_returnResponseInternalServerError_when_filesIdWasNotSaved() throws Exception{
        expectedException.expectCause(ServiceExceptionMatcher.assertMessage("Missing data for id 123"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/123"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }


    /*
[
  {
    "postId": 1,
    "id": 1,
    "name": "id labore ex et quam laborum",
    "email": "eliseo@gardner.biz",
    "body": "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium"
  },
  {
    "postId": 1,
    "id": 2,
    "name": "quo vero reiciendis velit similique earum",
    "email": "jayne_kuhic@sydney.com",
    "body": "est natus enim nihil est dolore omnis voluptatem numquam\net omnis occaecati quod ullam at\nvoluptatem error expedita pariatur\nnihil sint nostrum voluptatem reiciendis et"
  },
  {
    "postId": 1,
    "id": 3,
    "name": "odio adipisci rerum aut animi",
    "email": "nikita@garfield.biz",
    "body": "quia molestiae reprehenderit quasi aspernatur\naut expedita occaecati aliquam eveniet laudantium\nomnis quibusdam delectus saepe quia accusamus maiores nam est\ncum et ducimus et vero voluptates excepturi deleniti ratione"
  },
  {
    "postId": 1,
    "id": 4,
    "name": "alias odio sit",
    "email": "lew@alysha.tv",
    "body": "non et atque\noccaecati deserunt quas accusantium unde odit nobis qui voluptatem\nquia voluptas consequuntur itaque dolor\net qui rerum deleniti ut occaecati"
  },
  {
    "postId": 1,
    "id": 5,
    "name": "vero eaque aliquid doloribus et culpa",
    "email": "hayden@althea.biz",
    "body": "harum non quasi et ratione\ntempore iure ex voluptates in ratione\nharum architecto fugit inventore cupiditate\nvoluptates magni quo et"
  }
]
     */
    private String getJsonEncoded37Lines2064Bytes() {
        return "Ww0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgImlkIjogMSwNCiAgICAibmFtZSI6ICJpZCBsYWJvcmUgZXggZXQgcXVhbSBsYWJvcnVtIiwNCiAgICAiZW1haWwiOiAiZWxpc2VvQGdhcmRuZXIuYml6IiwNCiAgICAiYm9keSI6ICJsYXVkYW50aXVtIGVuaW0gcXVhc2kgZXN0IHF1aWRlbSBtYWduYW0gdm9sdXB0YXRlIGlwc2FtIGVvc1xudGVtcG9yYSBxdW8gbmVjZXNzaXRhdGlidXNcbmRvbG9yIHF1YW0gYXV0ZW0gcXVhc2lcbnJlaWNpZW5kaXMgZXQgbmFtIHNhcGllbnRlIGFjY3VzYW50aXVtIg0KICB9LA0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgImlkIjogMiwNCiAgICAibmFtZSI6ICJxdW8gdmVybyByZWljaWVuZGlzIHZlbGl0IHNpbWlsaXF1ZSBlYXJ1bSIsDQogICAgImVtYWlsIjogImpheW5lX2t1aGljQHN5ZG5leS5jb20iLA0KICAgICJib2R5IjogImVzdCBuYXR1cyBlbmltIG5paGlsIGVzdCBkb2xvcmUgb21uaXMgdm9sdXB0YXRlbSBudW1xdWFtXG5ldCBvbW5pcyBvY2NhZWNhdGkgcXVvZCB1bGxhbSBhdFxudm9sdXB0YXRlbSBlcnJvciBleHBlZGl0YSBwYXJpYXR1clxubmloaWwgc2ludCBub3N0cnVtIHZvbHVwdGF0ZW0gcmVpY2llbmRpcyBldCINCiAgfSwNCiAgew0KICAgICJwb3N0SWQiOiAxLA0KICAgICJpZCI6IDMsDQogICAgIm5hbWUiOiAib2RpbyBhZGlwaXNjaSByZXJ1bSBhdXQgYW5pbWkiLA0KICAgICJlbWFpbCI6ICJuaWtpdGFAZ2FyZmllbGQuYml6IiwNCiAgICAiYm9keSI6ICJxdWlhIG1vbGVzdGlhZSByZXByZWhlbmRlcml0IHF1YXNpIGFzcGVybmF0dXJcbmF1dCBleHBlZGl0YSBvY2NhZWNhdGkgYWxpcXVhbSBldmVuaWV0IGxhdWRhbnRpdW1cbm9tbmlzIHF1aWJ1c2RhbSBkZWxlY3R1cyBzYWVwZSBxdWlhIGFjY3VzYW11cyBtYWlvcmVzIG5hbSBlc3RcbmN1bSBldCBkdWNpbXVzIGV0IHZlcm8gdm9sdXB0YXRlcyBleGNlcHR1cmkgZGVsZW5pdGkgcmF0aW9uZSINCiAgfSwNCiAgew0KICAgICJwb3N0SWQiOiAxLA0KICAgICJpZCI6IDQsDQogICAgIm5hbWUiOiAiYWxpYXMgb2RpbyBzaXQiLA0KICAgICJlbWFpbCI6ICJsZXdAYWx5c2hhLnR2IiwNCiAgICAiYm9keSI6ICJub24gZXQgYXRxdWVcbm9jY2FlY2F0aSBkZXNlcnVudCBxdWFzIGFjY3VzYW50aXVtIHVuZGUgb2RpdCBub2JpcyBxdWkgdm9sdXB0YXRlbVxucXVpYSB2b2x1cHRhcyBjb25zZXF1dW50dXIgaXRhcXVlIGRvbG9yXG5ldCBxdWkgcmVydW0gZGVsZW5pdGkgdXQgb2NjYWVjYXRpIg0KICB9LA0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgImlkIjogNSwNCiAgICAibmFtZSI6ICJ2ZXJvIGVhcXVlIGFsaXF1aWQgZG9sb3JpYnVzIGV0IGN1bHBhIiwNCiAgICAiZW1haWwiOiAiaGF5ZGVuQGFsdGhlYS5iaXoiLA0KICAgICJib2R5IjogImhhcnVtIG5vbiBxdWFzaSBldCByYXRpb25lXG50ZW1wb3JlIGl1cmUgZXggdm9sdXB0YXRlcyBpbiByYXRpb25lXG5oYXJ1bSBhcmNoaXRlY3RvIGZ1Z2l0IGludmVudG9yZSBjdXBpZGl0YXRlXG52b2x1cHRhdGVzIG1hZ25pIHF1byBldCINCiAgfQ0KXQ==";
    }


    /*
[
  {
    "postId": 1,
    "name": "id labore ex et quam laborum",
    "email": "Eliseo@gardner.biz",
    "body": "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium nreiciendisqw"
  },
  {
    "postId": 1,
    "id": 2,
    "name": "quo vero reiciendis velit similique earum",
    "email": "Jayne_Kuhic@sydney.com",
    "body": "est natus enim nihil est dolore omnis voluptatem numquam\net omnis occaecati quod ullam at\nvoluptatem error expedita pariatur\nnihil sint nostrum voluptatem reiciendis et"
  },
  {
    "postId": 1,
    "id": 3,
    "name": "odio adipisci rerum aut animi",
    "email": "Nikita@garfield.biz",
    "body": "quia molestiae reprehenderit quasi aspernatur\naut expedita occaecati aliquam eveniet laudantium\nomnis quibusdam delectus saepe quia accusamus maiores nam est\ncum et ducimus et vero voluptates excepturi deleniti ratione"
  },
  {
    "postId": 1,
    "id": 4,
    "name": "alias odio sit",
    "email": "Lew@alysha.tv",
    "body": "non et atque\noccaecati deserunt quas accusantium unde odit nobis qui voluptatem\nquia voluptas consequuntur itaque dolor\net qui rerum deleniti ut occaecati"
  },
  {
    "postId": 1,
    "id": 5,
    "name": "vero eaque aliquid doloribus et culpa",
    "email": "Hayden@althea.biz",
    "body": "harum non quasi et ratione\ntempore iure ex voluptates in ratione\nharum architecto fugit inventore cupiditate\nvoluptates magni quo et"
  }
]
     */
    private String getJsonEncoded36Lines2064Bytes() {
        return "Ww0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgIm5hbWUiOiAiaWQgbGFib3JlIGV4IGV0IHF1YW0gbGFib3J1bSIsDQogICAgImVtYWlsIjogIkVsaXNlb0BnYXJkbmVyLmJpeiIsDQogICAgImJvZHkiOiAibGF1ZGFudGl1bSBlbmltIHF1YXNpIGVzdCBxdWlkZW0gbWFnbmFtIHZvbHVwdGF0ZSBpcHNhbSBlb3NcbnRlbXBvcmEgcXVvIG5lY2Vzc2l0YXRpYnVzXG5kb2xvciBxdWFtIGF1dGVtIHF1YXNpXG5yZWljaWVuZGlzIGV0IG5hbSBzYXBpZW50ZSBhY2N1c2FudGl1bSBucmVpY2llbmRpc3F3Ig0KICB9LA0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgImlkIjogMiwNCiAgICAibmFtZSI6ICJxdW8gdmVybyByZWljaWVuZGlzIHZlbGl0IHNpbWlsaXF1ZSBlYXJ1bSIsDQogICAgImVtYWlsIjogIkpheW5lX0t1aGljQHN5ZG5leS5jb20iLA0KICAgICJib2R5IjogImVzdCBuYXR1cyBlbmltIG5paGlsIGVzdCBkb2xvcmUgb21uaXMgdm9sdXB0YXRlbSBudW1xdWFtXG5ldCBvbW5pcyBvY2NhZWNhdGkgcXVvZCB1bGxhbSBhdFxudm9sdXB0YXRlbSBlcnJvciBleHBlZGl0YSBwYXJpYXR1clxubmloaWwgc2ludCBub3N0cnVtIHZvbHVwdGF0ZW0gcmVpY2llbmRpcyBldCINCiAgfSwNCiAgew0KICAgICJwb3N0SWQiOiAxLA0KICAgICJpZCI6IDMsDQogICAgIm5hbWUiOiAib2RpbyBhZGlwaXNjaSByZXJ1bSBhdXQgYW5pbWkiLA0KICAgICJlbWFpbCI6ICJOaWtpdGFAZ2FyZmllbGQuYml6IiwNCiAgICAiYm9keSI6ICJxdWlhIG1vbGVzdGlhZSByZXByZWhlbmRlcml0IHF1YXNpIGFzcGVybmF0dXJcbmF1dCBleHBlZGl0YSBvY2NhZWNhdGkgYWxpcXVhbSBldmVuaWV0IGxhdWRhbnRpdW1cbm9tbmlzIHF1aWJ1c2RhbSBkZWxlY3R1cyBzYWVwZSBxdWlhIGFjY3VzYW11cyBtYWlvcmVzIG5hbSBlc3RcbmN1bSBldCBkdWNpbXVzIGV0IHZlcm8gdm9sdXB0YXRlcyBleGNlcHR1cmkgZGVsZW5pdGkgcmF0aW9uZSINCiAgfSwNCiAgew0KICAgICJwb3N0SWQiOiAxLA0KICAgICJpZCI6IDQsDQogICAgIm5hbWUiOiAiYWxpYXMgb2RpbyBzaXQiLA0KICAgICJlbWFpbCI6ICJMZXdAYWx5c2hhLnR2IiwNCiAgICAiYm9keSI6ICJub24gZXQgYXRxdWVcbm9jY2FlY2F0aSBkZXNlcnVudCBxdWFzIGFjY3VzYW50aXVtIHVuZGUgb2RpdCBub2JpcyBxdWkgdm9sdXB0YXRlbVxucXVpYSB2b2x1cHRhcyBjb25zZXF1dW50dXIgaXRhcXVlIGRvbG9yXG5ldCBxdWkgcmVydW0gZGVsZW5pdGkgdXQgb2NjYWVjYXRpIg0KICB9LA0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgImlkIjogNSwNCiAgICAibmFtZSI6ICJ2ZXJvIGVhcXVlIGFsaXF1aWQgZG9sb3JpYnVzIGV0IGN1bHBhIiwNCiAgICAiZW1haWwiOiAiSGF5ZGVuQGFsdGhlYS5iaXoiLA0KICAgICJib2R5IjogImhhcnVtIG5vbiBxdWFzaSBldCByYXRpb25lXG50ZW1wb3JlIGl1cmUgZXggdm9sdXB0YXRlcyBpbiByYXRpb25lXG5oYXJ1bSBhcmNoaXRlY3RvIGZ1Z2l0IGludmVudG9yZSBjdXBpZGl0YXRlXG52b2x1cHRhdGVzIG1hZ25pIHF1byBldCINCiAgfQ0KXQ==";
    }

    /*
[
  {
    "postId": 1,
    "id": 1,
    "name": "id labore ex et quam laborum",
    "email": "Eliseo@gardner.biz",
    "body": "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium"
  },
  {
    "postId": 1,
    "id": 2,
    "name": "quo vero reiciendis velit similique earum",
    "email": "Jayne_Kuhic@sydney.com",
    "body": "est natus enim nihil est dolore omnis voluptatem numquam\net omnis occaecati quod ullam at\nvoluptatem error expedita pariatur\nnihil sint nostrum voluptatem reiciendis et"
  },
  {
    "postId": 1,
    "id": 3,
    "name": "odio adipisci rerum aut animi",
    "email": "Nikita@garfield.biz",
    "body": "quia molestiae reprehenderit quasi aspernatur\naut expedita occaecati aliquam eveniet laudantium\nomnis quibusdam delectus saepe quia accusamus maiores nam est\ncum et ducimus et vero voluptates excepturi deleniti ratione"
  },
  {
    "postId": 1,
    "id": 4,
    "name": "alias odio sit",
    "email": "Lew@alysha.tv",
    "body": "non et atque\noccaecati deserunt quas accusantium unde odit nobis qui voluptatem\nquia voluptas consequuntur itaque dolor\net qui rerum deleniti ut occaecati"
  },
  {
    "postId": 1,
    "id": 5,
    "name": "vero eaque aliquid doloribus et culpa",
    "email": "Hayden@althea.biz",
    "body": "harum non quasi et ratione\ntempore iure ex voluptates in ratione\nharum architecto fugit inventore cupiditate\nvoluptates magni quo et"
  }
]
     */
    private String getJsonEncoded37Lines2064BytesEmailUpperCase() {
        return "Ww0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgImlkIjogMSwNCiAgICAibmFtZSI6ICJpZCBsYWJvcmUgZXggZXQgcXVhbSBsYWJvcnVtIiwNCiAgICAiZW1haWwiOiAiRWxpc2VvQGdhcmRuZXIuYml6IiwNCiAgICAiYm9keSI6ICJsYXVkYW50aXVtIGVuaW0gcXVhc2kgZXN0IHF1aWRlbSBtYWduYW0gdm9sdXB0YXRlIGlwc2FtIGVvc1xudGVtcG9yYSBxdW8gbmVjZXNzaXRhdGlidXNcbmRvbG9yIHF1YW0gYXV0ZW0gcXVhc2lcbnJlaWNpZW5kaXMgZXQgbmFtIHNhcGllbnRlIGFjY3VzYW50aXVtIg0KICB9LA0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgImlkIjogMiwNCiAgICAibmFtZSI6ICJxdW8gdmVybyByZWljaWVuZGlzIHZlbGl0IHNpbWlsaXF1ZSBlYXJ1bSIsDQogICAgImVtYWlsIjogIkpheW5lX0t1aGljQHN5ZG5leS5jb20iLA0KICAgICJib2R5IjogImVzdCBuYXR1cyBlbmltIG5paGlsIGVzdCBkb2xvcmUgb21uaXMgdm9sdXB0YXRlbSBudW1xdWFtXG5ldCBvbW5pcyBvY2NhZWNhdGkgcXVvZCB1bGxhbSBhdFxudm9sdXB0YXRlbSBlcnJvciBleHBlZGl0YSBwYXJpYXR1clxubmloaWwgc2ludCBub3N0cnVtIHZvbHVwdGF0ZW0gcmVpY2llbmRpcyBldCINCiAgfSwNCiAgew0KICAgICJwb3N0SWQiOiAxLA0KICAgICJpZCI6IDMsDQogICAgIm5hbWUiOiAib2RpbyBhZGlwaXNjaSByZXJ1bSBhdXQgYW5pbWkiLA0KICAgICJlbWFpbCI6ICJOaWtpdGFAZ2FyZmllbGQuYml6IiwNCiAgICAiYm9keSI6ICJxdWlhIG1vbGVzdGlhZSByZXByZWhlbmRlcml0IHF1YXNpIGFzcGVybmF0dXJcbmF1dCBleHBlZGl0YSBvY2NhZWNhdGkgYWxpcXVhbSBldmVuaWV0IGxhdWRhbnRpdW1cbm9tbmlzIHF1aWJ1c2RhbSBkZWxlY3R1cyBzYWVwZSBxdWlhIGFjY3VzYW11cyBtYWlvcmVzIG5hbSBlc3RcbmN1bSBldCBkdWNpbXVzIGV0IHZlcm8gdm9sdXB0YXRlcyBleGNlcHR1cmkgZGVsZW5pdGkgcmF0aW9uZSINCiAgfSwNCiAgew0KICAgICJwb3N0SWQiOiAxLA0KICAgICJpZCI6IDQsDQogICAgIm5hbWUiOiAiYWxpYXMgb2RpbyBzaXQiLA0KICAgICJlbWFpbCI6ICJMZXdAYWx5c2hhLnR2IiwNCiAgICAiYm9keSI6ICJub24gZXQgYXRxdWVcbm9jY2FlY2F0aSBkZXNlcnVudCBxdWFzIGFjY3VzYW50aXVtIHVuZGUgb2RpdCBub2JpcyBxdWkgdm9sdXB0YXRlbVxucXVpYSB2b2x1cHRhcyBjb25zZXF1dW50dXIgaXRhcXVlIGRvbG9yXG5ldCBxdWkgcmVydW0gZGVsZW5pdGkgdXQgb2NjYWVjYXRpIg0KICB9LA0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgImlkIjogNSwNCiAgICAibmFtZSI6ICJ2ZXJvIGVhcXVlIGFsaXF1aWQgZG9sb3JpYnVzIGV0IGN1bHBhIiwNCiAgICAiZW1haWwiOiAiSGF5ZGVuQGFsdGhlYS5iaXoiLA0KICAgICJib2R5IjogImhhcnVtIG5vbiBxdWFzaSBldCByYXRpb25lXG50ZW1wb3JlIGl1cmUgZXggdm9sdXB0YXRlcyBpbiByYXRpb25lXG5oYXJ1bSBhcmNoaXRlY3RvIGZ1Z2l0IGludmVudG9yZSBjdXBpZGl0YXRlXG52b2x1cHRhdGVzIG1hZ25pIHF1byBldCINCiAgfQ0KXQ==";
    }

    /*
[
  {
    "postId": 1,
    "id": 1,
    "name": "id labore ex et quam laborum",
    "email": "Eliseo@gardner.biz",
    "body": "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium"
  },
  {
    "postId": 1,
    "id": 2,
    "name": "quo vero reiciendis velit similique earum",
    "email": "Jayne_Kuhic@sydney.com",
    "body": "est natus enim nihil est dolore omnis voluptatem numquam\net omnis occaecati quod ullam at\nvoluptatem error expedita pariatur\nnihil sint nostrum voluptatem reiciendis et"
  },
  {
    "postId": 1,
    "id": 3,
    "name": "odio adipisci rerum aut animi",
    "email": "Nikita@garfield.biz",
    "body": "quia molestiae reprehenderit quasi aspernatur\naut expedita occaecati aliquam eveniet laudantium\nomnis quibusdam delectus saepe quia accusamus maiores nam est\ncum et ducimus et vero voluptates excepturi deleniti ratione"
  }
]
     */
    private String getJsonEncoded23Lines1316BytesEmailUpperCase() {
        return "Ww0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgImlkIjogMSwNCiAgICAibmFtZSI6ICJpZCBsYWJvcmUgZXggZXQgcXVhbSBsYWJvcnVtIiwNCiAgICAiZW1haWwiOiAiRWxpc2VvQGdhcmRuZXIuYml6IiwNCiAgICAiYm9keSI6ICJsYXVkYW50aXVtIGVuaW0gcXVhc2kgZXN0IHF1aWRlbSBtYWduYW0gdm9sdXB0YXRlIGlwc2FtIGVvc1xudGVtcG9yYSBxdW8gbmVjZXNzaXRhdGlidXNcbmRvbG9yIHF1YW0gYXV0ZW0gcXVhc2lcbnJlaWNpZW5kaXMgZXQgbmFtIHNhcGllbnRlIGFjY3VzYW50aXVtIg0KICB9LA0KICB7DQogICAgInBvc3RJZCI6IDEsDQogICAgImlkIjogMiwNCiAgICAibmFtZSI6ICJxdW8gdmVybyByZWljaWVuZGlzIHZlbGl0IHNpbWlsaXF1ZSBlYXJ1bSIsDQogICAgImVtYWlsIjogIkpheW5lX0t1aGljQHN5ZG5leS5jb20iLA0KICAgICJib2R5IjogImVzdCBuYXR1cyBlbmltIG5paGlsIGVzdCBkb2xvcmUgb21uaXMgdm9sdXB0YXRlbSBudW1xdWFtXG5ldCBvbW5pcyBvY2NhZWNhdGkgcXVvZCB1bGxhbSBhdFxudm9sdXB0YXRlbSBlcnJvciBleHBlZGl0YSBwYXJpYXR1clxubmloaWwgc2ludCBub3N0cnVtIHZvbHVwdGF0ZW0gcmVpY2llbmRpcyBldCINCiAgfSwNCiAgew0KICAgICJwb3N0SWQiOiAxLA0KICAgICJpZCI6IDMsDQogICAgIm5hbWUiOiAib2RpbyBhZGlwaXNjaSByZXJ1bSBhdXQgYW5pbWkiLA0KICAgICJlbWFpbCI6ICJOaWtpdGFAZ2FyZmllbGQuYml6IiwNCiAgICAiYm9keSI6ICJxdWlhIG1vbGVzdGlhZSByZXByZWhlbmRlcml0IHF1YXNpIGFzcGVybmF0dXJcbmF1dCBleHBlZGl0YSBvY2NhZWNhdGkgYWxpcXVhbSBldmVuaWV0IGxhdWRhbnRpdW1cbm9tbmlzIHF1aWJ1c2RhbSBkZWxlY3R1cyBzYWVwZSBxdWlhIGFjY3VzYW11cyBtYWlvcmVzIG5hbSBlc3RcbmN1bSBldCBkdWNpbXVzIGV0IHZlcm8gdm9sdXB0YXRlcyBleGNlcHR1cmkgZGVsZW5pdGkgcmF0aW9uZSINCiAgfQ0KXQ==";
    }
}