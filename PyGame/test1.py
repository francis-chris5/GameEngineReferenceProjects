
import pygame
import math
from random import *



# initialize engine
pygame.init()
clock = pygame.time.Clock()
pygame.key.set_repeat(10, 10)
screenSize = (768, 512)
screen = pygame.display.set_mode(screenSize)
pygame.display.set_caption("Test Game 1")

# keep the score
score = 0
pygame.font.init()
scoreFont = pygame.font.SysFont("Times New Roman", 22)



#load in sprites
playerSprite = pygame.image.load("player.png")
playerRect = playerSprite.get_rect()
groundSprite = pygame.image.load("ground.png")
coinSprite = pygame.image.load("coin.png")
coinRect = coinSprite.get_rect()

# call objects
playerX = 123
playerY = 234
def player(x, y):
	screen.blit(playerSprite, (x, y))
	playerRect.x = x
	playerRect.y = y
	
	
def ground(x, y):
	screen.blit(groundSprite, (x, y))
	
	
coinX = math.floor(random() * 736)
coinY = math.floor(random() * 480)
def coin(x, y):
	screen.blit(coinSprite, (x, y))
	coinRect.x = x
	coinRect.y = y

# move player object
def playerControl(left, down, right, up):
	global playerX, playerY
	speed = 5
	playerX += speed * left
	playerX += speed * right
	playerY -= speed * up
	playerY -= speed * down



# main loop
playing = True
while playing:
	#event listeners
	for event in pygame.event.get():
		if event.type == pygame.QUIT:
			playing = False
		if event.type == pygame.KEYDOWN:
			if event.key == pygame.K_a:
				playerControl(-1, 0, 0, 0)
			elif event.key == pygame.K_s:
				playerControl(0, -1, 0, 0)
			elif event.key == pygame.K_d:
				playerControl(0, 0, 1, 0)
			elif event.key == pygame.K_w:
				playerControl(0, 0, 0, 1)
	

	
	
	#draw bg tiles
	for i in range(0, 768, 64):
		for j in range(0, 512, 64):
			ground(i, j)
			
	# check for player->coin collision or draw coin
	if playerRect.colliderect(coinRect):
		score += 1
		coinX = math.floor(random() * 736)
		coinY = math.floor(random() * 480)
		coin(coinX, coinY)
	else:
		coin(coinX, coinY)
		
	# draw player
	player(playerX, playerY)
	
	
	#show score
	showScore = scoreFont.render("SCORE: " + str(score), False, (12, 12, 12) )
	screen.blit(showScore, (30, 30))
	
	# update scene and frame-rate
	pygame.display.flip()
	clock.tick(60)



#exit the game
pygame.quit()