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

#include "cadid.h"

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

      if (!strcmp("hello", buffer)) {
	if (send(current_socket, "How are you ?\n", 15, 0) < 0) {
	  perror("send");
	  return EXIT_FAILURE;
	}
      }

      if (!strcmp("fine", buffer)) {
	if (send(current_socket, "MAICAISUPAIR\n", 14, 0) < 0) {
	  perror("send");
	  return EXIT_FAILURE;
	}
      }

      if (!strcmp("quit", buffer))
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
