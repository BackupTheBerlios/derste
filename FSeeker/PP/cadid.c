#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <sys/un.h>
#include <netinet/in.h>
#include <sys/un.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>
#include "cadid.h"

#define RET_OK "OK"
#define RET_ERR "ERR"
#define CMD_CREATE_PROCESS "CreateProcess"
#define CMD_DESTROY_PROCESS "DestroyProcess"
#define CMD_QUIT "Quit"

#define MAX_CHAR_PARAM   100
#define MAX_CHAR_COMMAND 100

void send_ok(const int socket) {
  char msg[MAX_CHAR_COMMAND];

  strcpy(msg, RET_OK);
  strcat(msg, "\n");

  if (send(socket, msg, strlen(msg) + 1, 0) < 0) {
    perror("send");
    exit(EXIT_FAILURE);
  }
}

void send_failure(const int socket) {
  char msg[MAX_CHAR_COMMAND];

  strcpy(msg, RET_ERR);
  strcat(msg, "\n");

  if (send(socket, msg, strlen(msg) + 1, 0) < 0) {
    perror("send");
    exit(EXIT_FAILURE);
  }
}

void send_ok_p(const int socket, const char *param) {
  char msg[MAX_CHAR_COMMAND];

  strcpy(msg, RET_OK);
  strcat(msg, " ");
  strcat(msg, param);
  strcat(msg, "\n");

  if (send(socket, msg, strlen(msg) + 1, 0) < 0) {
    perror("send");
    exit(EXIT_FAILURE);
  }
}

void send_failure_p(const int socket, const char *param) {
  char msg[MAX_CHAR_COMMAND];

  strcpy(msg, RET_ERR);
  strcat(msg, " ");
  strcat(msg, param);
  strcat(msg, "\n");

  if (send(socket, msg, strlen(msg) + 1, 0) < 0) {
    perror("send");
    exit(EXIT_FAILURE);
  }
}


pid_t create_process(char *app) {
  pid_t proc;

  proc = fork();

  /* Père */
  if (proc > 0)
    return proc;

  /* Fils */
  if (proc == 0) 
    if (execlp(app, NULL) == -1)
      exit(EXIT_FAILURE);

  /* Erreur */
  return 0;
}

int server_received(int socket, char *msg) {
  int nb_tokens;
  char cmd[MAX_CHAR_COMMAND];
  char param[MAX_CHAR_PARAM];
  pid_t new_proc;

  nb_tokens = sscanf(msg, "%s %s", cmd, param);
  
  if (nb_tokens > 0) {

    if (!strcmp(CMD_QUIT, cmd)) {
      send_ok(socket);
      return MSG_USER_QUIT;
    }

    if (!strcmp(CMD_CREATE_PROCESS, cmd)) {
      if (nb_tokens != 2) {
	send_failure_p(socket, CMD_CREATE_PROCESS " <application>");
      } else {	
	if ((new_proc = create_process(param))) {
	  sprintf(param, "%d", new_proc);
	  send_ok_p(socket, param);
	} else {
	  send_failure(socket);
	}
      }
    }

  }

  return 0;
}

int main() {
  int server_socket, current_socket;
  struct sockaddr_in server_address, client_address;
  socklen_t client_address_size;
  pid_t pid;
  char buffer[SERVER_BUFFER_SIZE];

  if ((server_socket = socket(PF_INET, SOCK_STREAM, 0)) < 0) {
    perror("ouverture du socket");
    return EXIT_FAILURE;
  }

  memset(&server_address, 0, sizeof server_address);
  server_address.sin_family = AF_INET;
  server_address.sin_addr.s_addr = htonl(INADDR_ANY);
  server_address.sin_port = htons(SERVER_PORT);
	
  if (bind(server_socket, (struct sockaddr *) &server_address, sizeof(server_address)) < 0) {
    perror("bind");
    return EXIT_FAILURE;
  }

  if (listen(server_socket, MAX_CLIENT) < 0) {
    perror("listen");
    return EXIT_FAILURE;
  }

  puts("En écoute ...");
    
  for (;;) {
  
    client_address_size = sizeof client_address;
    if ((current_socket = accept(server_socket, (struct sockaddr *) &client_address, &client_address_size)) < 0) {
      perror("accept");
      close(server_socket);
      return EXIT_FAILURE;
    }

    printf("Connection de %s ...\n", inet_ntoa(client_address.sin_addr));

    if ((pid = fork()) < 0) {
      perror("fork");
      return EXIT_FAILURE;
    }
    
    /* Dans le père */
    if (pid > 0) {
      close(current_socket);
      continue;
    }

    /* Dans le fils */
    close(server_socket);
    if (send(current_socket, "Hello network\n", 15, 0) < 0) {
      perror("send");
      return EXIT_FAILURE;
    }

    while (recv(current_socket, buffer, SERVER_BUFFER_SIZE, 0) > 0) {
      printf("Reçu : %s\n", buffer);
      fflush(stdout);

      if (server_received(current_socket, buffer) == MSG_USER_QUIT)
	break;
    }

    close(current_socket);
    printf("Fermeture de %s ...\n", inet_ntoa(client_address.sin_addr));
    fflush(stdout);

    return EXIT_SUCCESS;     
  }

  close(server_socket);
	
  return EXIT_SUCCESS;
}
